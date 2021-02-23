package com.kevin.tyrrell.diablo.diablo.runeword;

import com.kevin.tyrrell.diablo.diablo.item.ItemType;
import com.kevin.tyrrell.diablo.diablo.rune.ReadOnlyRuneMap;
import com.kevin.tyrrell.diablo.util.Saveable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
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

    private final Set<Runeword> watchedWords;
    /* Item types in which the user wishes to ignore. */
    private final Set<ItemType> ignoredTypes = EnumSet.noneOf(ItemType.class);
    /* Runewords in which the user wishes to ignore. */
    private final Set<Runeword> ignoredWords = new HashSet<>();

    /**
     * Constructs a runeword filter instance.
     *
     * @param runewords All possible runewords.
     */
    public RunewordFilter(final Stream<Runeword> runewords, final ReadOnlyRuneMap userRunes)
    {
        this.userRunes = requireNonNull(userRunes);
        watchedWords = requireNonNull(runewords).collect(Collectors.toSet());
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
