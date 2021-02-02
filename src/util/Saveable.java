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

package util;

import java.io.*;

/**
 * Defines an interface for objects which can be saved.
 *
 * TODO: Determine if more saving logic can be included into this class.
 *
 * @since 2.0
 */
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
