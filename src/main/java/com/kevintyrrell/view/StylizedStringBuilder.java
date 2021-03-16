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

import com.kevintyrrell.model.util.Builder;
import com.kevintyrrell.view.console.ConsoleColor;

import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

/**
 * String builder wrapper designed to handle the problem
 * of invalid lengths when using colored/stylized strings.
 *
 * @since 3.0
 */
public class StylizedStringBuilder implements Builder<String>
{
    private final StringBuilder builder;

    private int falseLength = 0;

    /**
     * Constructs an empty builder.
     */
    public StylizedStringBuilder()
    {
        this.builder = new StringBuilder();
    }

    /**
     * Constructs a builder with an initial string.
     *
     * @param str String to initialize the builder with.
     */
    public StylizedStringBuilder(final String str)
    {
        this.builder = new StringBuilder(requireNonNull(str));
    }

    /**
     * Declares an incoming stylized string into the builder.
     * Stylized strings report false character length, so you must declare them.
     *
     * @param str String to stylize.
     * @param color Color to style the string with.
     * @param callback Avenue for the caller to add the string to the builder.
     * @return Reference to the underlying builder.
     */
    public StringBuilder declare(final String str, final ConsoleColor color,
                                 BiConsumer<StringBuilder, String> callback)
    {
        falseLength += requireNonNull(color).code.length() + ConsoleColor.RESET.code.length();
        requireNonNull(callback).accept(builder, requireNonNull(color).wrap(requireNonNull(str)));
        return builder;
    }

    /**
     * @return Length of the stylized builder, ignoring style escapes.
     */
    public int length()
    {
        return builder.length() - falseLength;
    }

    /**
     * Retrieves the underlying builder for typical StringBuilder operations.
     *
     * @return Underlying string builder.
     */
    public StringBuilder builder()
    {
        return builder;
    }

    /**
     * Builds a new object based on the current attributes of the builder.
     *
     * @return Constructed object based on the current builder attributes.
     */
    @Override public String build()
    {
        return builder.toString();
    }

    /**
     * @return String representation of the builder.
     */
    @Override public String toString()
    {
        return build();
    }
}
