/*
 *     Application which tracks Runeword progress in the video game Diablo 2.
 *     Copyright (C) 2021  Kevin Tyrrell
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

package com.kevintyrrell.view;

import java.util.Arrays;

import static java.lang.String.join;
import static java.util.Collections.nCopies;
import static java.util.Objects.requireNonNull;

/**
 * Possible alignment orientation of fixed-width Strings.
 *
 * For information on typographic alignement see: https://en.wikipedia.org/wiki/Typographic_alignment
 *
 * @since 3.0
 */
public enum Alignment
{
    /**
     * Left-align: Text borders the left and remaining space is appended.
     */
    LEFT {@Override String alignImpl(final String line, final int width, final int len)
    {
        return line.concat(join("", nCopies(width - len, " ")));
    }},
    /**
     * Right-align: Text borders the right and remaining space is prepended.
     */
    RIGHT {@Override String alignImpl(final String line, final int width, final int len)
    {
        return join("", nCopies(width - len, " ")).concat(line);
    }},
    /**
     * Center-align: Text has no border and is evenly spaced on both sides, favoring the right side.
     */
    CENTER {@Override String alignImpl(final String line, final int width, final int len)
    {
        final int spaces = width - len;
        final String s = join("", nCopies(spaces / 2, " "));
        return s + line + s + ((spaces & 1) != 0 ? " " : "");
    }},
    /**
     * Justification: Word spacing is overridden, first/last words border the sides, and space is evenly distributed.
     */
    JUSTIFIED {@Override String alignImpl(final String line, final int width, final int len)
    {
        final String[] words = line.split("\\s+");
        if (words.length <= 1) // Edge case: one or less words cannot be justified.
            return line.concat(join("", nCopies(width - len, " ")));

        int spaces = width - Arrays.stream(words)
                .mapToInt(String::length)
                .sum();
        // Every word except the last gets N spaces appended to it.
        final int[] wordSpacing = new int[words.length - 1];
        Arrays.fill(wordSpacing, spaces / (words.length - 1));
        spaces %= words.length - 1; // Calculate the number of left-over spaces.

        final int mid = words.length / 2;
        /* Iterate for as long as we have extra spaces to give out. */
        for (int i = 0; i < spaces; i++)
            /* Start from the center and iterate outwards, heading left, then right. */
            wordSpacing[mid + (i + 1) / 2 - (i + 1) * (i & 1)]++;

        /* Build the resulting string with the specified spacing. */
        final StringBuilder builder = new StringBuilder(width);
        for (int i = 0; i < wordSpacing.length; i++)
            builder.append(words[i]).append(join("", nCopies(wordSpacing[i], " ")));
        return builder.append(words[words.length - 1]).toString();
    }};

    /**
     * Aligns a string in accordance with the alignment value.
     *
     * The specified width must be at least the size of the line itself.
     *
     * @param line Line to be aligned.
     * @param width Width of the aligned text.
     * @return Aligned string.
     */
    public String align(final String line, final int width)
    {
        final int len = requireNonNull(line).length();
        if (width < len) throw new IllegalArgumentException("Unable to align string to width of lesser size.");
        if (width == len) return line;
        return alignImpl(line, width, len);
    }

    abstract String alignImpl(final String line, final int width, final int len);
}
