/*
 * Application which tracks Runeword progress in the video game Diablo 2.
 * Copyright (C) 2018  Kevin Tyrrell
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.kevin.tyrrell.diablo.diablo.rune;

import com.kevin.tyrrell.diablo.console.Paragraph;
import com.kevin.tyrrell.diablo.diablo.item.ItemType;
import com.kevin.tyrrell.diablo.util.EnumExtendable;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kevin.tyrrell.diablo.diablo.item.ItemType.BODY_ARMOR;

/**
 * Defines all possible Runewords in Diablo 2 as of patch 1.11.
 *
 * @since 2.0
 */
public enum Runeword implements EnumExtendable
{

    /* Name of the Runeword. */
    private final String name;
    /* Word which activates the Runeword. */
    private final String word;
    /* Runes which the Runeword requires, in order. */
    private final Map<Rune, Integer> runes;
    /* Item types in which this Runeword can go into. */
    private final Set<ItemType> types;
    /* Required level for the Runeword. */
    private final int level;
    /* Summed rarity of all of the Runes. */
    private final double rarity;

    /* Threshold of when a Runeword should be tracked. */
    public static final float COMPLETION_THRESHOLD = 0.25f;
    /* Read-only map sorted by most rare Runeword -> most common. */
    public static final Map<Runeword, Integer> RANKINGS;

    /* Descriptions of every Runeword. */
    /* TODO: Only generate paragraphs when they are needed. */
    private static final Map<Runeword, Paragraph> descriptions = new EnumMap<>(Runeword.class);

    static
    {
        /* Rank every Runeword. The most rare Runeword is #1, followed by #2, etc. */
        final AtomicInteger rankCounter = new AtomicInteger();        
        RANKINGS = Arrays.stream(Runeword.values())
                .sorted(new Runeword.Comparator().reversed())
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Function.identity(), value -> rankCounter.incrementAndGet(), 
                                (a, b) -> a, LinkedHashMap::new), 
                        Collections::unmodifiableMap));

        // TODO: Clean this up and put it in a better spot.
        final Map<String, Runeword> t = Arrays.stream(values())
                .collect(Collectors.toMap(Runeword::getName, Function.identity()));
        final String regexp = "\\n$";
        final StringBuilder bd = new StringBuilder();

        final InputStream is = Runeword.class.getClassLoader().getResourceAsStream(RW_DESCRIPTION_PATH);
        final InputStreamReader isr = new InputStreamReader(is);

        try (final BufferedReader br = new BufferedReader(isr))
        {
            while (br.ready())
                bd.append(br.readLine()).append("\n");
            final String doc = bd.toString();
            bd.setLength(0);

            final Pattern p = Pattern.compile("(.+?)\\n((.|\\n)+?)\\n\\n", Pattern.DOTALL);
            final Matcher m = p.matcher(doc);

            while (m.find())
            {
                assert m.groupCount() >= 3;
                final String name = m.group(1);
                final String description = m.group(2);
                final Runeword rw = t.get(name);
                final Paragraph par = new Paragraph(Paragraph.Alignment.CENTER,
                        Stream.concat(
                                Stream.of(rw.getName() + '(' + rw.getLevel() + ')'),
                                Arrays.stream(description.split("\n")))
                        .toArray(String[]::new));
                descriptions.put(rw, par);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    Runeword(final String name, final int level, final Supplier<Set<ItemType>> types, final Rune... runes)
    {
        assert name != null;
        assert level >= 1;
        assert types != null;
        assert runes != null;
        assert runes.length > 0;

        this.name = name;
        this.level = level;
        /* Sort the Runes in descending order, map to Rune->Quantity, and make map read-only. */
        this.runes = Arrays.stream(runes)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.summingInt(e -> 1)),
                        Collections::unmodifiableMap));
        /* Only include Item types which can actually have enough sockets to support the Runeword. */
        this.types = types.get().stream()
                .filter(t -> t.getMaxSockets() >= runes.length)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> EnumSet.noneOf(ItemType.class)),
                        Collections::unmodifiableSet));
        word = Arrays.stream(runes).map(Rune::getName).collect(Collectors.joining());
        /* Sum the total rarity of all the Runes in the word. */
        rarity = this.runes.entrySet().stream()
                .mapToDouble(entry -> entry.getValue() * entry.getKey().getRarity())
                .sum();
    }

    /**
     * Calculates a progress percentage for the Runeword to be completed.
     * A Runeword is 100% complete when all Runes it requires are collected.
     * Runes have weighted rarities, so higher Runes make more of an impact.
     * @param runes Runes the user owns.
     * @return percentage of completion (between 0.00% and 1.00% inclusive).
     */
    public double calculateProgress(final Map<Rune, Integer> runes)
    {
        assert runes != null;
        return Math.round(this.runes.entrySet().stream()
                .filter(entry -> runes.containsKey(entry.getKey()))
                .mapToDouble(entry ->
                {
                    final Rune r = entry.getKey();
                    return Math.min(entry.getValue(), runes.get(r)) * calculateProgress(r);
                })
                /* Sum and round to two decimal places. */
                .sum() * 100.0) / 100.0;
    }

    /**
     * Calculates a progress percentage for the Runeword to be completed.
     * A Runeword is 100% complete when all Runes it requires are collected.
     * Runes have weighted rarities, so higher Runes make more of an impact.
     * @param rune Rune the user owns.
     * @return percentage of completion (between 0% and 1% inclusive).
     */
    public double calculateProgress(final Rune rune)
    {
        assert rune != null;
        return rune.getRarity() / rarity;
    }

    /**
     * @return Name of the Runeword.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return Sequence of Runes of the Runeword
     */
    public String getWord()
    {
        return word;
    }

    /**
     * @return Number of sockets required for the Runeword.
     */
    public int getRequiredSockets()
    {
        return runes.values().stream()
                .mapToInt(e -> e)
                .sum();
    }

    /**
     * @return Runes and their quantity for the Runeword.
     */
    public Map<Rune, Integer> getRunes()
    {
        return runes;
    }

    /**
     * @return Types the Runeword is applicable for.
     */
    public Set<ItemType> getTypes()
    {
        return types;
    }

    /**
     * @return Description of the Runeword, as if hovered-over in-game.
     */
    public Paragraph getDescription()
    {
        return descriptions.get(this);
    }

    /**
     * @return Required level to use the Runeword.
     */
    public int getLevel()
    {
        return level;
    }

    private static final Map<String, Runeword> fromString = Arrays.stream(Runeword.values())
            .collect(Collectors.collectingAndThen(
                    Collectors.toMap(k -> k.name().toLowerCase(), Function.identity()),
                    Collections::unmodifiableMap));

    /**
     * Looks up a Runeword from a String.
     * @param str String to use for the lookup.
     * @return Runeword corresponding to the String.
     */
    public static Runeword fromString(final String str)
    {
        assert str != null;
        return fromString.get(str);
    }

    @Override public String toString()
    {
        return "\"" + name + "\" " + word;
    }

    public static class Comparator implements java.util.Comparator<Runeword>
    {
        /**
         * Sums up the rarity of each Rune of a specified Runeword.
         * @param rw Runeword to calculate.
         * @return Rarity of the Runeword.
         */
        private static double calculateRarity(final Runeword rw)
        {
            assert rw != null;
            return rw.getRunes().entrySet().stream()
                    .mapToDouble(runeEntry -> runeEntry.getKey().getRarity() * runeEntry.getValue())
                    .sum();
        }

        @Override public int compare(final Runeword o1, final Runeword o2)
        {
            assert o1 != null;
            assert o2 != null;
            if (o1 == o2) return 0;
            return Double.compare(calculateRarity(o1), calculateRarity(o2));
        }
    }
}
