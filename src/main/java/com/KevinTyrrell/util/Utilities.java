/*
 *     TODO: ...
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

package com.KevinTyrrell.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Assortment of utility static methods.
 *
 * TODO: Reduce reliance on this class.
 * TODO: Much of this class should be inside of Console class.
 *
 * @since 2.0
 */
public final class Utilities
{
    /**
     * Rounds a number to a specified amount of significant figures.
     * @param d Number to round.
     * @param sigFigures Significant figures to round to.
     * @return Number rounded to the specified significant figured.
     */
    public static double roundSigFig(final double d, final int sigFigures)
    {
        assert sigFigures >= 1;
        final BigDecimal big = new BigDecimal(d);
        final MathContext mc = new MathContext(sigFigures);
        return big.round(mc).doubleValue();
    }

    /**
     * Parses through a stream of data, filtering out those who don't pass the specified callback function.
     * The parser callback should return null on objects which do not pass parsing.
     * @param inputs Stream of data to parse through.
     * @param parseCallback Callback function which handles the parsing of the data.
     * @param failureCallback Callback function for when a piece of data is fails parsing.
     * @param <V> Data type to parse through.
     * @param <T> Data type to parse to.
     * @return List of parsed objects.
     */
    public static <V,T> List<T> parseValues(final Stream<V> inputs, final Function<V, T> parseCallback,
                                            final Consumer<V> failureCallback)
    {
        assert inputs != null;
        assert parseCallback != null;

        return inputs
                .map(str ->
                {
                    final T parsed = parseCallback.apply(str);
                    if (parsed == null && failureCallback != null)
                        failureCallback.accept(str);
                    return parsed;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList));
    }

    /**
     * Parses through a stream of data, filtering out those who don't pass the specified callback function.
     * The parser callback should return null on objects which do not pass parsing.
     * @param inputs Stream of data to parse through.
     * @param parseCallback Callback function which handles the parsing of the data.
     * @param <V> Data type to parse through.
     * @param <T> Data type to parse to.
     * @return List of parsed objects.
     */
    public static <V,T> List<T> parseValues(final Stream<V> inputs, final Function<V, T> parseCallback)
    {
        return parseValues(inputs, parseCallback, null);
    }

    /**
     * Repeats a String a specified number of times.
     * @param str String to repeat.
     * @param times Times to repeat the String.
     * @return Repeated String.
     */
    public static String repeatString(final String str, final int times)
    {
        assert str != null;
        assert !str.isEmpty();
        assert times >= 0;
        if (times == 0) return "";
        return IntStream.range(0, times)
                .mapToObj(i -> str)
                .collect(Collectors.joining());
    }

    /**
     * Repeats a character a specified number of times.
     * @param ch Character to repeat.
     * @param times Times to repeat the character.
     * @return String of the repeated character.
     */
    public static String repeatChar(final char ch, final int times)
    {
        assert times >= 0;
        if (times == 0) return "";
        final char[] arr = new char[times];
        Arrays.fill(arr, ch);
        return new String(arr);
    }

    /**
     * Sleeps for a specified amount of milliseconds.
     * @param ms Milliseconds to sleep for.
     */
    public static void unsafeSleep(final long ms)
    {
        try
        {
            TimeUnit.MILLISECONDS.sleep(ms);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
