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
 * File Name:       Tracker
 * File Author:     Kevin Tyrrell
 * Date Created:    07/22/2018
 */

import console.Paragraph;
import console.TextTable;
import diablo.item.ItemType;
import diablo.rune.Rune;
import diablo.rune.Runeword;
import util.Saveable;
import util.Utilities;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static diablo.rune.Runeword.COMPLETION_THRESHOLD;

public final class Tracker
{
    /* Runes which the player possess. */
    private static final RuneLibrary runes = RuneLibrary.INSTANCE;
    /* Ignored Runewords and item types. */
    private static final IgnoreLibrary ignored = IgnoreLibrary.INSTANCE;
    /* Scanner for taking in user-input. */
    private static final Scanner sc = new Scanner(System.in);

    /* Inputs from the command line. */
    private static Deque<String> inputs = null;
    /* Maximum number of characters per line in the stdout. */
    private static final int CONSOLE_WIDTH = 80;

    /* The state the tracker is currently in. */
    private static State current = State.SPLASH_SCREEN;

    private enum State implements Stateable
    {
        /**
         * Splash screen shown at the start of the program.
         */
        SPLASH_SCREEN
        {
            final TextTable splash = new TextTable("Diablo II Runeword Tracker ".concat(Version.NUMBER))
                    .addRow(new Paragraph(Paragraph.Alignment.CENTER, CONSOLE_WIDTH,
                            "Kevin Tyrrell",
                            "https://github.com/KevinTyrrell/D2-Runeword-Tracker",
                            "Compiled On: ".concat(Version.COMPILED_ON.toString())));

            @Override
            public State next()
            {
                System.out.printf("%s\n\n", splash);
                Utilities.unsafeSleep(2500);
                return DISPLAYING_DATA;
            }
        },
        /**
         * Waiting for the user to enter an input.
         */
        AWAITING_COMMAND
        {
            @Override
            public State next()
            {

                System.out.println(Paragraph.Alignment.CENTER.align("COMMANDS", CONSOLE_WIDTH));
                System.out.println(Utilities.repeatChar('-', CONSOLE_WIDTH));
                final String MENU_FORMAT = "\t\t\t* %-8s\t(%s)\n";
                System.out.println(Arrays.stream(Command.values())
                        .map(c -> String.format(MENU_FORMAT, c.getName(), c.getDescription()))
                        .collect(Collectors.joining()));

                System.out.print("INPUT:\t");
                inputs = Arrays.stream(sc.nextLine()
                        .replaceAll("^a-zA-Z\\s_", "")
                        .toLowerCase()
                        .split("\\s+"))
                        .collect(Collectors.toCollection(ArrayDeque::new));
                System.out.print("\n\n");

                final String command = inputs.pollFirst();
                final Optional<Command> p = Arrays.stream(Command.values())
                        .filter(c -> c.getName().equals(command))
                        .findFirst();

                if (!p.isPresent())
                {
                    System.out.printf("Command \"%.10s\" was not recognized.\n\n", command);
                    return DISPLAYING_RUNES;
                }

                return p.get().getLeadsTo();
            }
        },
        /**
         * Runes are being added to the tracker.
         */
        ADDING_RUNES
        {
            @Override
            public State next()
            {
                Rune.parseRuneQuantities(inputs.stream())
                        .forEach(runes::add);

                if (!runes.hasUnsavedChanges())
                    return DISPLAYING_RUNES;
                if (!runes.save())
                    System.out.printf(Saveable.SAVE_FAILED_FMT, "Rune Library");
                return DISPLAYING_DATA;
            }
        },
        /* Runes are being tossed from the tracker. */
        TOSSING_RUNES
        {
            @Override
            public State next()
            {
                Rune.parseRuneQuantities(inputs.stream())
                        .forEach((rune, integer) ->
                        {
                            if (!runes.toss(rune, integer))
                                System.out.printf("Error: Insufficient Rune(s) to toss %s (x%d).\n\n",
                                        rune.getName(), integer);
                        });

                if (!runes.hasUnsavedChanges())
                    return DISPLAYING_RUNES;
                if (!runes.save())
                    System.out.printf(Saveable.SAVE_FAILED_FMT, "Rune Library");
                return DISPLAYING_DATA;
            }
        },
        /**
         * Runewords / Item types are being ignored.
         */
        IGNORING_PREFS
        {
            @Override
            public State next()
            {
                final Set<String> rejectedA = new LinkedHashSet<>(), rejectedB = new LinkedHashSet<>();
                final List<ItemType> types = Utilities.parseValues(inputs.stream(),
                        ItemType::fromString, rejectedA::add);
                final List<Runeword> rws = Utilities.parseValues(inputs.stream(),
                        Runeword::fromString, rejectedB::add);
                /* Intersection of Set A and Set B. */
                rejectedA.retainAll(rejectedB);
                rejectedA.forEach(str -> System.out.printf(
                        "Error: No such Runeword/Item type of \"%.10s\" exists.\n\n", str));

                types.forEach(ignored::toggle);
                rws.forEach(ignored::toggle);

                if (!ignored.hasUnsavedChanges())
                    return DISPLAYING_RUNES;
                if (!ignored.save())
                    System.out.printf(Saveable.SAVE_FAILED_FMT, "Ignore Preferences");

                return DISPLAYING_DATA;
            }
        },
        /**
         * Displaying calculated table data.
         */
        DISPLAYING_DATA
        {
            public State next()
            {
                if (runes.get().isEmpty()) return DISPLAYING_RUNES;

                /* Completion progress towards all tracked Runewords. */
                final Map<Runeword, Double> rwProgress = new TreeMap<>(new Runeword.Comparator().reversed());

                /* Watch only the Runewords the user cares for and that they are in progress towards. */
                final Set<Runeword> watchedWords = Runeword.RANKINGS.keySet().stream()
                        /* Ignore Runewords that the user has ignored. */
                        .filter(rw -> !ignored.getRunewords().contains(rw))
                        /* Ignore Runewords whose bases are all ignored. */
                        .filter(rw -> !ignored.getTypes().containsAll(rw.getTypes()))
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

                final TextTable table = new TextTable("RUNEWORD", "RANK", "STATUS", "WORD", "BASE(S)");
                watchedWords.stream()
                        .map(rw -> Stream.of(
                                new Paragraph(Paragraph.Alignment.CENTER,
                                        rw.getName() + '(' + rw.getLevel() + ')'),
                                new Paragraph(Paragraph.Alignment.CENTER, Runeword.RANKINGS.get(rw).toString()),
                                new Paragraph(String.format("%.5s%%", rwProgress.get(rw) * 100)),
                                new Paragraph(rw.getWord()),
                                new Paragraph(rw.getTypes().stream()
                                        .filter(((Predicate<ItemType>)ignored.getTypes()::contains).negate())
                                        .map(ItemType::toString)
                                        .collect(Collectors.joining(", "))))
                                .toArray(Paragraph[]::new))
                        .forEach(table::addRow);
                System.out.println(table);

                /* Indicate to the user what Rune(s) he should get rid of, if any. */
                final Map<Rune, Integer> trashRunes = runes.findInsignificantRunes(rwProgress);
                if (trashRunes.size() > 0)
                {
                    final String tossStr = trashRunes.entrySet().stream()
                            .map(runeNumEntry -> runeNumEntry.getKey().getName() + " (x" + runeNumEntry.getValue() + ")")
                            .collect(Collectors.joining(", "));
                    final String tossDiv = Utilities.repeatString("-", tossStr.length());
                    final String TOSS_MSG = "Detected insignificant Rune(s). Selling them is highly recommended.";
                    System.out.printf("%s\n%s\n%s\n%s\n\n", TOSS_MSG, tossDiv, tossStr, tossDiv);
                }

                return DISPLAYING_RUNES;
            }
        },
        DISPLAYING_RUNES
        {
            @Override
            public State next()
            {
                final String title = Paragraph.Alignment.CENTER.align("RUNE LIBRARY", CONSOLE_WIDTH);
                final String div = Utilities.repeatChar('~', CONSOLE_WIDTH);
                final Paragraph runePar = Paragraph.enforcedWidth(Paragraph.Alignment.CENTER,
                        runes.toString(), CONSOLE_WIDTH);
                runePar.setWidth(CONSOLE_WIDTH);
                System.out.printf("%s\n%s\n%s\n%s\n\n", title, div, runePar.toString(), div);

                return AWAITING_COMMAND;
            }
        },
        /**
         * Program is in the process of shutting down.
         */
        SHUTTING_DOWN
        {
            @Override
            public State next()
            {
                System.out.println("Closing Diablo II Runeword Tracker.\nYour changes have already been saved.");
                return null;
            }
        }
    }

    /**
     * Runs the Tracker until it is shutdown.
     */
    public static void run()
    {
        do
        {
            /* Advance the state continuously. */
            current = current.next();
            /* Run the tracker until a shutdown occurs. */
        } while (current != null);
    }

    private enum Command
    {
        ADD("add", "space separated list of Runes", State.ADDING_RUNES),
        TOSS("toss", "space separated list of Runes", State.TOSSING_RUNES),
        IGNORE("ignore", "space separated list of Runewords/Item types", State.IGNORING_PREFS),
        QUIT("quit", "ends the program", State.SHUTTING_DOWN);

        /* Name of the Command. */
        private final String name;
        /* Description of the Command. */
        private final String description;
        /* State which the Command leads to. */
        private final State leadsTo;

        Command(final String name, final String description, final State leadsTo)
        {
            assert name != null;
            assert description != null;
            assert leadsTo != null;
            this.name = name;
            this.description = description;
            this.leadsTo = leadsTo;
        }

        /**
         * @return Name of the Command.
         */
        public String getName()
        {
            return name;
        }

        /**
         * @return Description of the Command.
         */
        public String getDescription()
        {
            return description;
        }

        /**
         * @return State which the Command leads to.
         */
        public State getLeadsTo()
        {
            return leadsTo;
        }
    }

    interface Stateable
    {
        /**
         * Performs an action and advances to the next state.
         * @return The next State the machine should switch to.
         */
        State next();
    }
}
