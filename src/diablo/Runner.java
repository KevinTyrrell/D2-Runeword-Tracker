/*
Copyright © 2018 Kevin Tyrrell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package diablo;

/*
 * File Name:       Runner
 * File Author:     Kevin Tyrrell
 * Date Created:    03/09/2018
 */

import diablo.item.ItemType;
import diablo.rune.Rune;
import diablo.rune.Runeword;
import util.Utilities;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Runner
{
    /* Runes which the player posses. */
    private static final RuneLibrary runes = RuneLibrary.INSTANCE;
    /* Ignored runewords and item types. */
    private static final IgnoreLibrary ignored = IgnoreLibrary.INSTANCE;

    /* Scanner to handle user input. */
    private static final Scanner sc = new Scanner(System.in);
    /* Flag which determines if the program should end its runtime. */
    private static boolean terminate = false;
    
    /* Threshold of when a runeword should be tracked. */
    private static final float COMPLETION_THRESHOLD = 25.0f;

    /**
     * Parses through specified string(s) and returns Enum values of the class which match.
     * @param enumeration Enum class in which to match the String constants with.
     * @param inputs String(s) to parse through.
     * @param <T> Enum data type.
     * @return Enum values of the Enum class who correspond to the String constant(s).
     */
    private static <T extends Enum<T>> List<T> parseEnums(final Class<T> enumeration, final String... inputs)
    {
        assert enumeration != null;
        return Arrays.stream(inputs)
                .map(str ->
                {
                    try
                    {
                        return Enum.valueOf(enumeration, str.toUpperCase());
                    }
                    catch (final NullPointerException | IllegalArgumentException e)
                    {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        Collections::unmodifiableList));
    }

    /**
     * Prints the main menu and waits for user input.
     * @return False if an invalid input was entered.
     */
    private static boolean waitMenu()
    {
        final String MENU = "COMMANDS---------\n" +
                "* add\t\t(space separated list of runes)\n" +
                "* toss\t\t(space separated list of runes)\n" +
                "* ignore\t(space separated list of runewords/item types)\n" +
                "* quit\t\t(ends the program)\n" +
                "\n-----Input: ";
        
        final String runeLibStr = runes.toString();
        final String runeLibDiv = Utilities.repeatString("~", runeLibStr.length());
        System.out.printf("\nRUNE LIBRARY\n%s\n%s\n%s\n\n", runeLibDiv, runeLibStr, runeLibDiv);

        System.out.print(MENU);
        final String[] inputs = sc.nextLine()
                .replaceAll("[^a-zA-Z\\s_]+", "")
                .toLowerCase()
                .split("\\s+");
        System.out.println();

        final String command = inputs[0];
        inputs[0] = null; // Makes parsing a bit faster.
        switch(command)
        {
        case "add": parseEnums(Rune.class, inputs)
                .forEach(runes::add); break;
        case "toss": parseEnums(Rune.class, inputs)
                .forEach(runes::toss); break;
        case "ignore":
            parseEnums(ItemType.class, inputs)
                    .forEach(ignored::toggle);
            parseEnums(Runeword.class, inputs)
                    .forEach(ignored::toggle); break;
        case "quit": System.err.println("Goodbye"); terminate = true; return true;
        default: System.err.println("Command was unrecognized."); return false;
        }
            
        /* Save the libraries to the hard disk. */
        Stream.of(runes, ignored)
                /* For some reason if you method reference here, you will get an exception. -JDK 9.0.4 */
                .forEach(e -> e.save());
        return true;
    }

    /**
     * Prints the splash screen to the console window.
     */
    private static void printSplashScreen()
    {
        final String title = "Diablo II Runeword Tracker ".concat(Version.NUMBER);
        final String name = "Kevin Tyrrell";
        final String url = "https://github.com/KevinTyrrell/D2-Runeword-Tracker";
        
        final int width = 80;
        final String dv1 = Utilities.repeatString("~", (width - 2 - title.length()) / 2);
        final String dv2 = Utilities.repeatString(" ", (width - 2 - name.length()) / 2);
        final String dv3 = Utilities.repeatString(" ", (width - 2 - url.length()) / 2);
        
        System.out.printf("\n\n\n%s %s %s\n\n%s %s %s\n\n%s %s %s\n%s\n\n\n",
                dv1, title, dv1, dv2, name, dv2, dv3, url, dv3, Utilities.repeatString("~", width)); 
        Utilities.unsafeSleep(2500);
    }

    public static void main(String[] args)
    {
        /* Display the splash screen. */
        printSplashScreen();
        
        /* Rank every runeword. The most expensive Runeword is #1, followed by #2, etc. */
        final AtomicInteger rankCounter = new AtomicInteger();
        final Map<Runeword, Integer> wordRankings = Arrays.stream(Runeword.values())
                .sorted(new Runeword.Comparator().reversed())
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(key -> key, value -> rankCounter.incrementAndGet(),
                                (a, b) -> a, LinkedHashMap::new),
                        Collections::unmodifiableMap));
        
        /* Function which grades the player's completion of a runeword, from 0 (no progress) to 1 (completed). */
        final ToDoubleFunction<Runeword> progressFunc = rw -> rw.getRunes().entrySet().stream()
                .mapToDouble(entry ->
                {
                    final Rune r = entry.getKey();
                    final Map<Rune, Integer> runeCount = runes.get();
                    return !runeCount.containsKey(r) ? 0
                            : Math.min(entry.getValue(), runeCount.get(r)) * (1 / r.getRarity());
                })
                .sum() /
                rw.getRunes().entrySet().stream()
                        .mapToDouble(entry -> entry.getValue() * (1 / entry.getKey().getRarity()))
                        .sum();

        
        // ####################################################################################################
        final String divider = String.join("", Collections.nCopies(100, "."));
        // # RUNEWORD            # RANK # COMPLETION # WORD               #   BASES
        final String fmt = divider.concat("\n| %-19s | %-4s | %-10s | %-18s | %-33s |\n");
        
        do
        {
            /* Watch only the runewords the user cares for and that they are in progress towards. */
            final Set<Runeword> watchedWords = wordRankings.keySet().stream()
                    /* Ignore runewords that the user has ignored. */
                    .filter(rw -> !ignored.getRunewords().contains(rw))
                    /* Ignore runewords whose bases are all ignored. */
                    .filter(rw -> !rw.getTypes().stream()
                            .allMatch(ignored.getTypes()::contains))
                    
                    .filter(rw -> rw.getRunes().entrySet().stream()
                            .anyMatch(entry -> runes.get().containsKey(entry.getKey())))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toCollection(LinkedHashSet::new),
                            Collections::unmodifiableSet));

            System.out.printf(fmt, "RUNEWORD", "RANK", "COMPLETION", "WORD", "BASE(S)");
            watchedWords.stream()
                    .map(rw -> Map.entry(rw, 100 * progressFunc.applyAsDouble(rw)))
                    .filter(rwProgress -> rwProgress.getValue() >= COMPLETION_THRESHOLD)
                    .map(rwProgress ->
                    {
                        final Runeword rw = rwProgress.getKey();
                        return new String[] {
                                rw.getName(),
                                wordRankings.get(rw).toString(),
                                String.format("%.2f%%", rwProgress.getValue()),
                                rw.getWord(),
                                rw.getTypes().stream()
                                        .filter(it -> !ignored.getTypes().contains(it))
                                        .map(ItemType::toString)
                                        .collect(Collectors.joining(", "))
                        };
                    })
                    .forEach(line -> System.out.printf(fmt, line));
            System.out.println(divider);
            
            /* Display the main menu and take in user-input. */
            while (!waitMenu());
        } while (!terminate);
    }
}
