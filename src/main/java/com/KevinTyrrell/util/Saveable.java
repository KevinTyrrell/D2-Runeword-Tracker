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

package com.KevinTyrrell.util;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

/**
 * Defines an interface for objects which can be saved.
 *
 * @since 2.0
 */
public interface Saveable extends Serializable
{
    /**
     * Directory in which Saveable objects are written to the storage medium.
     * The absolute path is based off of the program's working directory.
     */
    String SAVE_DIRECTORY = "saves/";

    /**
     * File extension of the serialized Saveable object(s).
     */
    String SAVE_EXTENSION = ".ser";

    /**
     * Flag provided by the inheriting class which controls
     * whether or not the object has unsaved changes present.
     * The flag itself is completely managed by Saveable.
     *
     * This method should only be called by the Saveable class.
     * For flagging unsaved changes, call `flagUnsavedChanges()`.
     *
     * @return AtomicBoolean instance variable of the inheriting class.
     */
    AtomicBoolean getUnsavedChanges();

    /**
     * Flags the Saveable object as having unsaved changes.
     *
     * On the next `save()` call, the changes will be saved.
     */
    default void flagUnsavedChanges()
    {
        requireNonNull(getUnsavedChanges()).set(true);
    }

    /**
     * Called during loading or saving to retrieve the filename.
     * By default, filename will be the name of the base class.
     *
     * To change this behavior, override this method.
     * Returned filename should not contain a file extension.
     * Full path of the file will be dictated as such:
     *  SAVE_DIRECTORY + getFileName() + SAVEABLE_EXTENSION
     *
     * @see Saveable#formatRelativePath(String)
     * @return filename which will be saved/loaded from the storage medium.
     */
    default String getFileName()
    {
        return getClass().getName();
    }

    /**
     * Writes the Saveable object to the storage medium.
     * The save directory and file extension are dictated by the
     * `SAVE_DIRECTORY` and `SAVE_EXTENSION` constants respectively.
     * The default filename is the base class name of the Saveable object.
     *
     * @return true if saving to the storage medium was successful.
     */
    default boolean save()
    {
        final AtomicBoolean unsavedChanges = requireNonNull(getUnsavedChanges());
        if (!unsavedChanges.get()) return true; // No changes to save.

        final File f = new File(formatRelativePath(requireNonNull(getFileName())));

        try (final FileOutputStream fos = new FileOutputStream(f);
             final ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(this);
            unsavedChanges.set(false);
            return true;
        }
        catch (final IOException e) { e.printStackTrace(); }

        return false;
    }

    /**
     * Attempts to load the Saveable object from the storage medium.
     * Returns null if the expected serialized file does not exist,
     * or if an expected exception occurs during file I/O.
     *
     * For default filenames (uses class name), this method should be used.
     * If custom filenames are desired, call `load(String)` instead.
     * In addition, override `getFileName()` to specify the same filename.
     *
     * @see Saveable#load(String)
     * @see Saveable#getFileName()
     * @see Saveable#save()
     * @return Saveable object from the storage medium, or null.
     */
    @SuppressWarnings("unchecked")
    static <T extends Saveable> T load(final Class<T> cls)
    {
        return load(requireNonNull(cls).getName());
    }

    /**
     * Attempts to load the Saveable object from the storage medium.
     * Returns null if the expected serialized file does not exist,
     * or if an expected exception occurs during file I/O.
     *
     * @see Saveable#load(Class)
     * @see Saveable#getFileName()
     * @see Saveable#save()
     * @return Saveable object from the storage medium, or null.
     */
    @SuppressWarnings("unchecked")
    static <T extends Saveable> T load(final String filename)
    {
        final File f = new File(formatRelativePath(requireNonNull(filename)));

        try (final FileInputStream fis = new FileInputStream(f);
             final ObjectInputStream ois = new ObjectInputStream(fis))
        {
            return (T) ois.readObject();
        }
        catch (final FileNotFoundException ignored) { }
        catch (final IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /* Formats the relative path for the file which is being saved or loaded. */
    private static String formatRelativePath(final String filename)
    {
        assert filename != null;
        return String.format("%s%s%s", SAVE_DIRECTORY, filename, SAVE_EXTENSION);
    }
}
