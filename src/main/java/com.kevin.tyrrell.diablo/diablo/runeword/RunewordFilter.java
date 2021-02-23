package com.kevin.tyrrell.diablo.diablo.runeword;

import com.kevin.tyrrell.diablo.diablo.item.ItemType;
import com.kevin.tyrrell.diablo.diablo.rune.ReadOnlyRuneMap;
import com.kevin.tyrrell.diablo.util.CachedValue;
import com.kevin.tyrrell.diablo.util.Saveable;

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
    /* Runes in which the user currently owns. */
    private final ReadOnlyRuneMap userRunes;

    private final CachedValue<Stream<Runeword>> watchedWords;
    /* Item types in which the user wishes to ignore. */
    private final Set<ItemType> ignoredTypes = EnumSet.noneOf(ItemType.class);
    /* Runewords in which the user wishes to ignore. */
    private final Set<Runeword> ignoredWords = new HashSet<>();

    /**
     * Constructs a runeword filter instance.
     *
     * @param runewords All possible runewords.
     */
    public RunewordFilter(final Collection<Runeword> runewords, final ReadOnlyRuneMap userRunes)
    {
        this.userRunes = requireNonNull(userRunes);
        watchedWords = null;
    }

    /**
     * Filter or restore a specific item type.
     *
     * Lazy evaluation - Only filters the runewords on demand.
     * Multiple calls may be needed for a single item type,
     * so streaming all children of an item type is advised.
     *
     * @param type Type to filter or restore.
     * @return true if the item type is filtered, false if restored.
     */
    public boolean filter(final ItemType type)
    {
        return filter(ignoredTypes, type);
    }

    /**
     * Filter or restore a specific runeword.
     *
     * Lazy evaluation - Only filters the runewords on demand.
     * TODO: Parent item types may cause problems here.
     *
     * @param runeword Runeword to filter or restore.
     * @return true if the item type is filtered, false if restored.
     */
    public boolean filter(final Runeword runeword)
    {
        return filter(ignoredWords, runeword);
    }

    /* Helper method to avoid repeated code. */
    private <T> boolean filter(final Set<T> container, final T value)
    {
        final boolean rval;
        if (container.contains(requireNonNull(value)))
            rval = container.remove(value);
        else rval = container.add(value);
        flagUnsavedChanges();
        watchedWords.invalidate();
        return rval;
    }

    /**
     * Evaluates the filters and outputs the current subset of non-filtered runewords.
     *
     * @return Stream of non-filtered runewords.
     */
    public Stream<Runeword> getRunewords()
    {
        return watchedWords.get();
    }

    private Stream<Runeword> evaluateFilters(final Collection<Runeword> runewords)
    {
        assert runewords != null;
        assert !runewords.isEmpty();
//        runewords.stream()
//                .filter(rw ->
        return null;
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
