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

package com.kevin.tyrrell.diablo.console;

import java.util.*;
import java.util.stream.Collectors;

import static com.kevin.tyrrell.diablo.util.Utilities.repeatChar;

/**
 * Collection of lines of strings which can be aligned.
 *
 * TODO: Assess whether Paragraph should remain immutable.
 * TODO: Implement justified or distributed flags.
 * TODO: Allow for list<String> constructor.
 *
 * @since 2.0
 */
public final class Paragraph
{
    /* Current width of the Paragraph. */
    private int width;
    /* Current alignment of the Paragraph. */
    private Alignment alignment;

    /* Smallest width the paragraph can support. */
    private final int minimumWidth;
    /* Lines which the Paragraph consists of. */
    private final List<String> lines;

    /*
     * Constructs a Paragraph based on a String and the alignment / width it must adhere to.
     * @param alignment Alignment of the text.
     * @param content String which will be formed into a paragraph.
     * @param maxWidth Max width for the Paragraph to take up.
     */
    public static Paragraph enforcedWidth(final Alignment alignment, final String content, final int maxWidth)
    {
        assert alignment != null;
        assert content != null;
        assert maxWidth >= 0;

        final List<String> lines = new ArrayList<>();
        final Deque<String> words = Arrays.stream(weedOut(content).split("\\s"))
                .collect(Collectors.toCollection(ArrayDeque::new));

        final List<String> line = new ArrayList<>();
        int charSum = 0;

        while (!words.isEmpty())
        {
            final String word = words.pollFirst();
            assert word != null;
            assert word.length() <= maxWidth;

            /* The line cannot overflow the specified width limit. */
            if (word.length() + charSum + line.size() > maxWidth)
            {
                lines.add(String.join(" ", line));
                line.clear();
                charSum = 0;
            }

            line.add(word);
            charSum += word.length();
        }

        lines.add(String.join(" ", line));

        return new Paragraph(alignment, lines.toArray(new String[0]));
    }

    /**
     * Constructs a Paragraph given each individual line and the alignment / width it must adhere to.
     * The width cannot be smaller than the minimum width of the Paragraph.
     * @param alignment Alignment of the text.
     * @param width Width of the Paragraph.
     * @param lines Each line of the Paragraph.
     */
    public Paragraph(final Alignment alignment, final int width, final String... lines)
    {
        assert alignment != null;
        assert width >= 0;

        this.alignment = alignment;
        this.lines = Arrays.stream(lines)
                .map(Paragraph::weedOut)
                .collect(Collectors.toList());
        minimumWidth = this.lines.stream()
                .mapToInt(String::length)
                .max().getAsInt();
        assert width >= minimumWidth;
        this.width = width;
    }

    /**
     * Constructs a Paragraph given each individual line and the alignment it must adhere to.
     * The minimum width will be set to the length of the longest line in the Paragraph.
     * @param alignment Alignment of the text.
     * @param lines Each line of the Paragraph.
     */
    public Paragraph(final Alignment alignment, final String... lines)
    {
        assert alignment != null;

        this.alignment = alignment;
        this.lines = Arrays.stream(lines)
                .map(Paragraph::weedOut)
                .collect(Collectors.toList());
        width = minimumWidth = this.lines.stream()
                .mapToInt(String::length)
                .max().getAsInt();
    }

    /**
     * Constructs a Paragraph given each individual line.
     * The minimum width will be set to the length of the longest line in the Paragraph.
     * Default leftward alignment is used.
     * @param lines Each line of the Paragraph.
     */
    public Paragraph(final String... lines)
    {
        this(Alignment.LEFT, lines);
    }

    /**
     * @param line Line number of the Paragraph.
     * @return Line of the Paragraph.
     */
    public String getLine(final int line)
    {
        assert line >= 0;
        assert line < lines.size();
        return alignment.align(lines.get(line), width);
    }

    /**
     * @return Current width of the Paragraph.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Sets the current width of the Paragraph.
     * The new width of the Paragraph cannot go lower than
     * the minimum width of the Paragraph. If a smaller
     * Paragraph is needed, a new Paragraph should be
     * constructed using a specified maximum width.
     * @param width
     */
    public void setWidth(final int width)
    {
        assert width >= minimumWidth;
        this.width = width;
    }

    /**
     * @return Current alignment of the Paragraph.
     */
    public Alignment getAlignment()
    {
        return alignment;
    }

    /**
     * @param alignment Paragraph alignment to change to.
     */
    public void setAlignment(final Alignment alignment)
    {
        assert alignment != null;
        this.alignment = alignment;
    }

    /**
     * @return Minimum amount of width needed for the Paragraph.
     */
    public int getMinimumWidth()
    {
        return minimumWidth;
    }

    /**
     * @return Number of lines in the Paragraph.
     */
    public int getLines()
    {
        return lines.size();
    }

    /**
     * @return String representation of the Paragraph.
     */
    @Override
    public String toString()
    {
        return lines.stream()
                .map(l -> alignment.align(l, width))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Weed out any impurities that could disrupt the functionality of the Paragraph.
     * @param s String to parse.
     * @return String safe to be used in the Paragraph.
     */
    private static String weedOut(final String s)
    {
        assert s != null;
        return s.replaceAll("[\\s]{2,}|[\t]+", " ").replaceAll("\n\r", "").trim();
    }

    /**
     * Possible alignments of a Paragraph.
     */
    public enum Alignment
    {
        LEFT, RIGHT, CENTER;

        /**
         * Aligns text to this alignment.
         * @param str String to align.
         * @param width Width of the area in which the text will be aligned.
         * @return Aligned String.
         */
        public String align(final String str, final int width)
        {
            final int dx = width - str.length();
            assert dx >= 0;
            if (dx == 0) return str;

            final StringBuilder sb = new StringBuilder(width);

            switch (this)
            {
                case LEFT:
                    sb.append(str).append(repeatChar(' ', dx)); break;
                case RIGHT:
                    sb.append(repeatChar(' ', dx)).append(str); break;
                default:
                    final String half = repeatChar(' ', dx / 2);
                    sb.append(half);
                    if (dx % 2 != 0) sb.append(' ');
                    sb.append(str).append(half);
                    break;
            }

            return sb.toString();
        }
    }
}
