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

package diablo;

/*
 * File Name:       IgnoreLibrary
 * File Author:     Kevin Tyrrell
 * Date Created:    07/16/2018
 */

import diablo.item.ItemType;
import diablo.rune.Runeword;
import util.Utilities;

import java.io.*;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiFunction;

public enum IgnoreLibrary implements Saveable
{
    /* Singleton instance. */
    INSTANCE;

    /* Runewords which are ignored. */
    final EnumSet<Runeword> runewords;
    /* Item types which are ignored. */
    final EnumSet<ItemType> types;

    private final static String FILE_NAME_RUNEWORDS = "IgnoredWords.ser",
            FILE_NAME_TYPES = "IgnoredTypes.ser";
    
    IgnoreLibrary()
    {
        final EnumSet<Runeword> tempRw = Utilities.readSerializable(new File(FILE_NAME_RUNEWORDS));
        final EnumSet<ItemType> tempIt = Utilities.readSerializable(new File(FILE_NAME_TYPES));
        runewords = tempRw != null ? tempRw : EnumSet.noneOf(Runeword.class);
        types = tempIt != null ? tempIt : EnumSet.noneOf(ItemType.class);
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

    /**
     * Saves the object to the storage medium.
     * 
     * @return True if the object was saved.
     */
    @Override public boolean save()
    {
        final BiFunction<Serializable, File, Boolean> func = (serializable, file) ->
        {
            assert serializable != null;
            assert file != null;
            
            try (final FileOutputStream fos = new FileOutputStream(file))
            {
                try (final ObjectOutputStream oos = new ObjectOutputStream(fos))
                {
                    oos.writeObject(serializable);
                    return true;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            return false;
        };
        
        return func.apply(runewords, new File(FILE_NAME_RUNEWORDS)) 
                && func.apply(types, new File(FILE_NAME_TYPES));
    }
}
