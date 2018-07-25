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

package util;

/*
 * File Name:       Utilities
 * File Author:     Kevin Tyrrell
 * Date Created:    07/16/2018
 */

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Time;
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
