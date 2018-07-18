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
import java.util.concurrent.TimeUnit;

public final class Utilities
{
    /**
     * Repeats a String 'n' times.
     * @param str String to be repeated.
     * @param times Amount of times for the String to repeat.
     * @return Repeated String.
     */
    public static String repeatString(final String str, final int n)
    {
        assert str != null;
        assert !str.isEmpty();
        assert n > 0;
        return new String(new char[n]).replace("\0", str);
    }
    
    /**
     * Reads a serialized object from the storage medium.
     * @param f File to read from.
     * @param <T> Data type of the object.
     * @return Object read from the file or null if the object could not be read.
     */
    public static <T> T readSerializable(final File f)
    {
        assert f != null;
        
        try (final FileInputStream fis = new FileInputStream(f))
        {
            try (final ObjectInputStream ois = new ObjectInputStream(fis))
            {
                return (T) ois.readObject();
            }
            catch (ClassNotFoundException ignored) { }
        }
        catch (IOException ignored) { }
        
        return null;
    }

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
