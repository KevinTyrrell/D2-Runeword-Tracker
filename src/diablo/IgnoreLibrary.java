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

package diablo;

import diablo.item.ItemType;
import diablo.rune.Runeword;
import util.Saveable;

import java.io.*;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Singleton collection handling ignore preferences.
 *
 * @since 2.0
 */
public enum IgnoreLibrary implements Saveable
{
    /* Singleton instance. */
    INSTANCE;

    /* Item types which are ignored. */
    final EnumSet<ItemType> types;
    /* Runewords which are ignored. */
    final EnumSet<Runeword> runewords;

    private final static String FILE_NAME_RUNEWORDS = "IgnoredWords.ser",
            FILE_NAME_TYPES = "IgnoredTypes.ser";
    
    IgnoreLibrary()
    {
        final EnumSet<ItemType> ti = Saveable.loadSerializable(new File(FILE_NAME_TYPES));
        final EnumSet<Runeword> tr = Saveable.loadSerializable(new File(FILE_NAME_RUNEWORDS));
        types = ti != null ? ti : EnumSet.noneOf(ItemType.class);
        runewords = tr != null ? tr : EnumSet.noneOf(Runeword.class);
    }

    /**
     * Toggles the Runeword to be ignored or un-ignored.
     * @param word Runeword to modify.
     * @return True if the Runeword is now ignored.
     */
    public boolean toggle(final Runeword word)
    {
        return toggle(word, runewords);
    }

    /**
     * Toggles the type to be ignored or un-ignored.
     * @param type Type to modify.
     * @return True if the type is now ignored.
     */
    public boolean toggle(final ItemType type)
    {
        return toggle(type, types);
    }

    /**
     * Toggles a value in a set.
     * If the value does not exist, it is added.
     * If the value exists, it is removed.
     * @param value Value to search for.
     * @param set Set to modify.
     * @param <T> Data type of the Enum value.
     * @return True if the value now exists in the set.
     */
    private <T extends Enum<T>> boolean toggle(final T value, final EnumSet<T> set)
    {
        assert value != null;
        assert set != null;

        unsavedChanges = true;
        if (set.remove(value))
            return true;
        return set.add(value);
    }
    
    /**
     * @return Read-only set of Runewords which are ignored.
     */
    public Set<Runeword> getRunewords()
    {
        return Collections.unmodifiableSet(runewords);
    }

    /**
     * @return Read-only set of Item Types which are ignored.
     */
    public Set<ItemType> getTypes()
    {
        return Collections.unmodifiableSet(types);
    }

    private boolean unsavedChanges = false;

    /**
     * Saves the object to the storage medium.
     *
     * @return True if the object was saved.
     */
    @Override
    public boolean save()
    {
        unsavedChanges = !Saveable.saveSerializable(runewords, new File(FILE_NAME_RUNEWORDS))
                || !Saveable.saveSerializable(types, new File(FILE_NAME_TYPES));
        return !unsavedChanges;
    }

    /**
     * Indicates if the Object has changes made since the last save.
     *
     * @return True if changes have been made since the last save.
     */
    @Override
    public boolean hasUnsavedChanges()
    {
        return unsavedChanges;
    }
}
