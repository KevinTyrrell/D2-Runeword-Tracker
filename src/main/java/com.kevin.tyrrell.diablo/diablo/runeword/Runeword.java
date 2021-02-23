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

package com.kevin.tyrrell.diablo.diablo.runeword;

import com.kevin.tyrrell.diablo.diablo.item.ItemType;
import com.kevin.tyrrell.diablo.diablo.rune.ReadOnlyRuneMap;
import com.kevin.tyrrell.diablo.diablo.rune.Rune;
import com.kevin.tyrrell.diablo.diablo.rune.RuneMap;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines a Runeword which is a specific arrangement of various Runes.
 *
 * @since 2.0
 */
public class Runeword implements ReadOnlyRuneMap
{
    private final String name;
    /* Correct rune order. */
    private final String word;
    /* Description of the affixes. */
    private final String description;
    /* Required character level. */
    private final int level;
    /* Number of open sockets required. */
    private final int requiredSockets;
    /* Immutable set of compatible bases. */
    private final Set<ItemType> types;
    /* Runes and their quantities. */
    private final ReadOnlyRuneMap runes;

    public Runeword(final String name, final int level, final String description,
                    final Stream<ItemType> types, final Stream<Rune> runes)
    {
        if (level <= 0 || level > 99)
            throw new IllegalArgumentException("Runeword level must be within bounds [1, 99]");
        this.name = requireNonNull(name);
        this.level = level;
        this.description = requireNonNull(description);
        this.types = types.flatMap(ItemType::stream)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(ItemType.class)));
        // Iterate through runes and grab the word, sockets, and rune counts.
        final AtomicInteger requiredSockets = new AtomicInteger();
        final StringBuilder builder = new StringBuilder();
        this.runes = new RuneMap(runes
                .filter(r -> // abuse filter to double-dip on stream.
                {
                    requiredSockets.incrementAndGet();
                    builder.append(r.getName()); return true;
                }));
        this.requiredSockets = requiredSockets.get();
        word = builder.toString();
        /* Runeword names are guaranteed to be unique. */
        hashCode = name.hashCode();
    }

    /**
     * @return Read-only view of the rune map.
     */
    @Override public Map<Rune, Integer> getRunes()
    {
        return runes.getRunes();
    }

    /**
     * Appraises the rarity of a rune map.
     * <p>
     * Appraisal is directly correlated to the rarity
     * of the runes and their respective quantities.
     *
     * @return Appraisal of the rune map.
     */
    @Override public double appraise()
    {
        return runes.appraise();
    }

    /**
     * @return Name of the Runeword.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return Description of the Runeword.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @return Sequence of Runes to activate the Runeword.
     */
    public String getWord()
    {
        return word;
    }

    /**
     * @return Required level to use the Runeword.
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * @return Number of sockets required for the Runeword.
     */
    public int getRequiredSockets()
    {
        return requiredSockets;
    }

    /**
     * @return Types of items in which the Runeword can be placed in.
     */
    public Set<ItemType> getTypes()
    {
        return types;
    }

    /* Cache hash code for faster lookups. */
    final int hashCode;
    @Override public int hashCode()
    {
        return hashCode;
    }
}
