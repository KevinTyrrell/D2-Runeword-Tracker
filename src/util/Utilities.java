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

public final class Utilities
{
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
}
