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

package com.kevintyrrell.model.diablo.runeword;

import com.kevintyrrell.model.diablo.ItemType;
import com.kevintyrrell.model.diablo.rune.ReadOnlyRuneMap;
import com.kevintyrrell.model.util.Saveable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines a storage unit for Runewords which offers
 * the capability to filter subsets of Runewords.
 *
 * @since 3.0
 */
public final class RunewordFilter implements Saveable
{
    /* Reference to container of runewords. */
    private final Collection<Runeword> runewords;
    /* Item types in which the user wishes to ignore. */
    private final Set<ItemType> filteredTypes = EnumSet.noneOf(ItemType.class), filteredTypesRO;
    /* Runewords in which the user wishes to ignore. */
    private final Set<Runeword> filteredWords = new HashSet<>(), filteredWordsRO;
    /* Runes in which the player owns. */
    private final ReadOnlyRuneMap runes;

    /* Minimum completion for runewords to avoid being filtered. */
    private float progressThreshold = DEFAULT_COMPLETION_THRESHOLD;

    private static final float DEFAULT_COMPLETION_THRESHOLD = 0.15f;

    /**
     * @param runewords Collection of all known runewords.
     */
    public RunewordFilter(final Collection<Runeword> runewords, final ReadOnlyRuneMap runes)
    {
        this.runewords = requireNonNull(runewords);
        this.runes = requireNonNull(runes);
        filteredTypesRO = Collections.unmodifiableSet(filteredTypes);
        filteredWordsRO = Collections.unmodifiableSet(filteredWords);
    }

    /**
     * Filter or restore a specific item type.
     *
     * Lazy evaluation - Only filters the runewords on demand.
     * Item type parameter must be a concrete item type.
     *
     * @param type Type to filter or restore.
     * @return true if the item type is filtered, false if restored.
     * @see ItemType#isConcrete()
     */
    public boolean filter(final ItemType type)
    {
        if (!requireNonNull(type).isConcrete())
            throw new IllegalArgumentException("Item type parameter must be concrete.");
        return filter(filteredTypes, type);
    }

    /**
     * Filter or restore a specific runeword.
     *
     * Lazy evaluation - Only filters the runewords on demand.
     *
     * @param runeword Runeword to filter or restore.
     * @return true if the item type is filtered, false if restored.
     */
    public boolean filter(final Runeword runeword)
    {
        return filter(filteredWords, runeword);
    }

    /* Helper method to avoid repeated code. */
    private <T> boolean filter(final Set<T> container, final T value)
    {
        final boolean rval;
        if (container.contains(requireNonNull(value)))
            rval = container.remove(value);
        else rval = container.add(value);
        flagUnsavedChanges();
        return rval;
    }

    /**
     * Evaluates the filters and outputs the current subset of non-filtered runewords.
     *
     * Due to needing to check player rune progress, it is not possible to cache this operation.
     *
     * @return Stream of non-filtered runewords.
     */
    public Stream<Runeword> getRunewords()
    {
        return runewords.stream()
                .filter(rw -> !filteredWords.contains(rw))
                .filter(rw -> runes.progressTowards(rw) >= progressThreshold)
                .filter(rw -> !filteredTypes.containsAll(rw.getTypes()));
    }

    /**
     * @return Read-only set of item types being filtered.
     */
    public Set<ItemType> getFilteredTypes()
    {
        return filteredTypesRO;
    }

    /**
     * @return Read-only set of runewords being filtered.
     */
    public Set<Runeword> getFilteredWords()
    {
        return filteredWordsRO;
    }

    /**
     * @return Progress threshold from [0, 1] for runewords to avoid being filtered.
     */
    public float getProgressThreshold()
    {
        return progressThreshold;
    }

    /**
     * @param progressThreshold Progress threshold from [0, 1] for runewords to avoid being filtered.
     */
    public void setProgressThreshold(final float progressThreshold)
    {
        if (progressThreshold < 0.0f || progressThreshold > 1.0f)
            throw new IllegalArgumentException("Progress threshold must of the domain [0, 1].");
        this.progressThreshold = progressThreshold;
    }

    private final AtomicBoolean unsavedChanges = new AtomicBoolean();

    /**
     * Flag provided by the inheriting class which controls
     * whether or not the object has unsaved changes present.
     * The flag itself is completely managed by Saveable.
     * <p>
     * This method should only be called by the Saveable class.
     * For flagging unsaved changes, call `flagUnsavedChanges()`.
     *
     * @return AtomicBoolean instance variable of the inheriting class.
     */
    @Override public AtomicBoolean getUnsavedChanges()
    {
        return unsavedChanges;
    }
}
