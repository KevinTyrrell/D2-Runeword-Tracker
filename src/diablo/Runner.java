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

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static diablo.rune.Runeword.COMPLETION_THRESHOLD;

public final class Runner
{
    /* Runes which the player possess. */
    private static final RuneLibrary runes = RuneLibrary.INSTANCE;
    /* Ignored Runewords and item types. */
    private static final IgnoreLibrary ignored = IgnoreLibrary.INSTANCE;

    /* Scanner to handle user input. */
    private static final Scanner sc = new Scanner(System.in);
    /* Flag which determines if the program should end its runtime. */
    private static boolean terminate = false;

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
        
        final String RUNE_LIB_STR = "RUNE LIBRARY";
        final String runeLibStr = runes.toString();
        final String runeLibDiv = Utilities.repeatString("~", 
                Math.max(runeLibStr.length(), RUNE_LIB_STR.length()));
        System.out.printf("\n%s\n%s\n%s\n%s\n\n", RUNE_LIB_STR, runeLibDiv, runeLibStr, runeLibDiv);

        System.out.print(MENU);
        final String[] inputs = sc.nextLine()
                .replaceAll("[^a-zA-Z\\s_]+", "")
                .toLowerCase()
                .split("\\s+");
        System.out.println();

        final String command = inputs[0];
        inputs[0] = null; // Allows us to double-dip on the user input.
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
        
        // ####################################################################################################
        final String divider = String.join("", Collections.nCopies(100, "."));
        // # RUNEWORD            # RANK # COMPLETION # WORD               #   BASES
        final String fmt = divider.concat("\n| %-23s | %-4s | %-6s | %-18s | %-33.33s |\n");
        final DecimalFormat df = new DecimalFormat("###.00");
        
        do
        {
            /* Completion progress towards all tracked Runewords. */
            final Map<Runeword, Double> rwProgress = new TreeMap<>(new Runeword.Comparator().reversed());
            
            /* Watch only the Runewords the user cares for and that they are in progress towards. */
            final Set<Runeword> watchedWords = Runeword.RANKINGS.keySet().stream()
                    /* Ignore Runewords that the user has ignored. */
                    .filter(rw -> !ignored.getRunewords().contains(rw))
                    /* Ignore Runewords whose bases are all ignored. */
                    .filter(rw -> !rw.getTypes().stream()
                            .allMatch(ignored.getTypes()::contains))
                    /* Filter out Runewords which we lack enough progress towards. */
                    .filter(rw -> 
                    {
                        final double progress = rw.calculateProgress(runes.get());
                        if (progress >= COMPLETION_THRESHOLD)
                        {
                            rwProgress.put(rw, progress);
                            return true;
                        }
                        
                        return false;
                    })
                    .collect(Collectors.collectingAndThen(
                            Collectors.toCollection(LinkedHashSet::new),
                            Collections::unmodifiableSet));
                    
            /* Print out the table of data. */
            System.out.printf(fmt, "RUNEWORD", "RANK", "STATUS", "WORD", "BASE(S)");
            watchedWords.stream()
                    .map(rw -> Map.entry(rw, 100 * rwProgress.get(rw)))
                    .map(rwProgEntry ->
                    {
                        final Runeword rw = rwProgEntry.getKey();
                        return Stream.of(
                                rw.getName() + "(" + rw.getLevel() + ")",
                                " ".concat(Runeword.RANKINGS.get(rw).toString()),
                                String.format("%.5s%%", df.format(rwProgEntry.getValue())),
                                rw.getWord(),
                                rw.getTypes().stream()
                                        .filter(it -> !ignored.getTypes().contains(it))
                                        .map(ItemType::toString)
                                        .collect(Collectors.joining(", ")))
                                .toArray(String[]::new);
                    })
                    .forEach(line -> System.out.printf(fmt, line));
            System.out.println(divider);

            /* Indicate to the user what Rune(s) he should get rid of, if any. */
            final Map<Rune, Integer> trashRunes = runes.findInsignificantRunes(rwProgress);
            if (trashRunes.size() > 0)
            {
                final String tossStr = trashRunes.entrySet().stream()
                        .map(runeNumEntry -> runeNumEntry.getKey().getName() + " (x" + runeNumEntry.getValue() + ")")
                        .collect(Collectors.joining(", "));
                final String tossDiv = Utilities.repeatString("-", tossStr.length());
                final String TOSS_MSG = "Detected insignificant Rune(s). Selling them is highly recommended.";
                System.out.printf("\n%s\n%s\n%s\n%s\n\n", TOSS_MSG, tossDiv, tossStr, tossDiv);
            }
            
            /* Display the main menu and take in user-input. */
            while (!waitMenu());
        } while (!terminate);
    }
}
