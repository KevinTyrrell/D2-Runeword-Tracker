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
 * File Name:       Saveable
 * File Author:     Kevin Tyrrell
 * Date Created:    07/16/2018
 */

import java.io.*;

public interface Saveable
{
    /**
     * Saves the object to the storage medium.
     * @return True if the object was saved.
     */
    boolean save();

    /**
     * Indicates if the Object has changes made since the last save.
     * @return True if changes have been made since the last save.
     */
    boolean hasUnsavedChanges();

    /* Message format for errors when a Saveable fails to save. */
    String SAVE_FAILED_FMT = "Failed to save %s to the storage medium.\n\n";

    /**
     * Saves the Serializable object to the storage medium.
     * @param s Serializable object to save.
     * @param f File to save the object inside of.
     * @return True if saving was successful.
     */
    static boolean saveSerializable(final Serializable s, final File f)
    {
        assert s != null;
        assert f != null;

        try (final FileOutputStream fos = new FileOutputStream(f);
             final ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(s);
            return true;
        }
        catch (final IOException ignored) { }

        return false;
    }

    /**
     * Loads a Serializable object from the storage medium.
     * @param f File to load Object from.
     * @param <T> Generic type of the object.
     * @return Serializable object, or null if loading failed.
     */
    @SuppressWarnings("unchecked")
    static <T> T loadSerializable(final File f)
    {
        assert f != null;

        try (final FileInputStream fis = new FileInputStream(f);
             final ObjectInputStream ois = new ObjectInputStream(fis))
        {

            return (T) ois.readObject();
        }
        catch (IOException | ClassNotFoundException ignored) { }

        return null;
    }
}
