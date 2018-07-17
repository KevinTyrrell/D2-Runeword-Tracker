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
 * File Name:       RuneLibrary
 * File Author:     Kevin Tyrrell
 * Date Created:    03/17/2018
 */

import diablo.rune.Rune;
import util.Utilities;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RuneLibrary implements Saveable
{
    INSTANCE;
    
    private final TreeMap<Rune, Integer> runes;

    private static final String FILE_NAME = "RuneLib.ser";
    
    RuneLibrary()
    {
        TreeMap<Rune, Integer> tempRunes = Utilities.readSerializable(new File(FILE_NAME));
        runes = tempRunes != null ? tempRunes : new TreeMap<>(Comparator.reverseOrder());
    }

    /**
     * Adds a rune to the library.
     * @param r Rune to add.
     */
    public void add(final Rune r)
    {
        assert r != null;
        runes.merge(r, 1, Integer::sum);
    }

    /**
     * Adds a rune or runes to the library.
     * @param r Rune(s) to add.
     */
    public void add(final Rune... r)
    {
        Arrays.stream(r)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((rune, aLong) -> runes.merge(rune, aLong.intValue(), Integer::sum));
    }

    /**
     * Removes a rune from the library.
     * @param r Rune to remove.
     */
    public void toss(final Rune r)
    {
        assert r != null;
        runes.computeIfPresent(r, (rune, i) -> i > 1 ? i - 1 : null);
    }

    /**
     * Removes a rune or runes from the library.
     * @param r Rune(s) to remove.
     */
    public void toss(final Rune... r)
    {
        Arrays.stream(r)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((rune, l) -> runes.computeIfPresent(rune, 
                        (rn, i) -> i > l.intValue() ? i - l.intValue() : null));
    }

    /**
     * @return map of runes corresponding with their quantity.
     */
    public Map<Rune, Integer> get()
    {
        return Collections.unmodifiableMap(runes);
    }
    
    /**
     * Saves the object to the storage medium.
     *
     * @return True if the object was saved.
     */
    @Override public boolean save()
    {
        try (final FileOutputStream fos = new FileOutputStream(new File(FILE_NAME)))
        {
            try (final ObjectOutputStream oos = new ObjectOutputStream(fos))
            {
                oos.writeObject(runes);
                return true;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return false;
    }

    @Override public String toString()
    {
        return runes.entrySet().stream()
                .map(rune -> rune.getKey().getName() + "(x" + rune.getValue() + ')')
                .collect(Collectors.joining(", "));
    }
}
