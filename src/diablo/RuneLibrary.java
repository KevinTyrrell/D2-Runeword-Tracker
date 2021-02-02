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

import diablo.rune.Rune;
import diablo.rune.Runeword;
import util.Saveable;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static diablo.rune.Runeword.COMPLETION_THRESHOLD;

/**
 * Singleton collection handling Runes.
 *
 * @since 2.0
 */
public enum RuneLibrary implements Saveable
{
    INSTANCE;

    /* Runes which the player owns, in sorted order. */
    // TODO: This can almost certainly be made as an EnumMap.
    private final TreeMap<Rune, Integer> runes;

    private static final String FILE_NAME = "RuneLib.ser";
    
    RuneLibrary()
    {
        final TreeMap<Rune, Integer> t = Saveable.loadSerializable(new File(FILE_NAME));
        runes = t != null ? t : new TreeMap<>(Comparator.reverseOrder());
    }

    /**
     * Adds a Rune to the library.
     * @param r Rune to add.
     */
    public void add(final Rune r)
    {
        add(r, 1);
    }

    /**
     * Adds a Rune with a specified quantity to the library.
     * @param r Rune to add.
     * @param quantity Quantity of the Rune to add.
     */
    public void add(final Rune r, final int quantity)
    {
        assert r != null;
        assert quantity > 0;

        runes.merge(r, quantity, Integer::sum);
        unsavedChanges = true;
    }

    /**
     * Attempts to remove a Rune from the library.
     * @param r Rune to remove.
     * @return False if the library has no such rune.
     */
    public boolean toss(final Rune r)
    {
        return toss(r, 1);
    }

    /**
     * Attempts to remove a Rune with a specified quantity from the library.
     * @param r Rune to remove.
     * @param quantity Quantity of that Rune to remove.
     * @return False if the Rune does not exist or if insufficient quantity.
     */
    public boolean toss(final Rune r, final int quantity)
    {
        assert r != null;
        assert quantity > 0;

        final AtomicBoolean b = new AtomicBoolean(false);

        runes.compute(r, (rune, integer) ->
        {
            if (integer != null && integer >= quantity)
            {
                b.set(true);
                final int diff = integer - quantity;
                return diff > 0 ? diff : null;
            }

            return integer;
        });

        return unsavedChanges = b.get();
    }

    /**
     * Evaluates the Runes the player owns, progress towards the Runeword(s) they are tracking,
     * and outputs a subset of Runes they own which give very little benefit towards the tracked Runeword(s).
     * @param rwProgress Current progress towards each Runeword's completion.
     * @return Collection of Runes and their quantities which should be tossed.
     */
    public Map<Rune, Integer> findInsignificantRunes(final Map<Runeword, Double> rwProgress)
    {
        assert rwProgress != null;
        
        /* Assume all Runes should be discarded. */
        final Map<Rune, Integer> toToss = new EnumMap<>(Rune.class);
        toToss.putAll(runes);

        rwProgress.forEach((rw, value) ->
        {
            final double progressLeft = 1.0 - value;
            rw.getRunes().entrySet().stream()
                    .map(runeNumEntry -> Stream.generate(runeNumEntry::getKey)
                            .limit(runeNumEntry.getValue()))
                    .flatMap(Function.identity())
                    .filter(toToss::containsKey)
                    .forEach(r ->
                    {
                        final double runeProg = rw.calculateProgress(r);
                        /* Is this Rune worth at least [CT%] of the remaining needed progress? */
                        if (runeProg / COMPLETION_THRESHOLD >= progressLeft - runeProg)
                            /* Rune is valued towards current goals, keep it. */
                            toToss.computeIfPresent(r, (rn, h) -> h > 1 ? h - 1 : null);
                    });
        });

        return toToss.entrySet().stream()
                /* Hel Runes can be used for un-socketing, omit them. */
                .filter(entry -> entry.getKey() != Rune.HEL)
                /* High Runes should not be considered for tossing. */
                .filter(entry -> !entry.getKey().isHighRune())
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }

    /**
     * @return map of Runes corresponding with their quantity.
     */
    public Map<Rune, Integer> get()
    {
        return Collections.unmodifiableMap(runes);
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
        unsavedChanges = !Saveable.saveSerializable(runes, new File(FILE_NAME));
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


    @Override public String toString()
    {
        return runes.entrySet().stream()
                .map(rune -> rune.getKey().getName() + "(x" + rune.getValue() + ')')
                .collect(Collectors.joining(", "));
    }
}
