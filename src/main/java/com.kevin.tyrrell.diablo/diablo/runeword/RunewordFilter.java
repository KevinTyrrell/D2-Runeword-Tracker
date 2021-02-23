package com.kevin.tyrrell.diablo.diablo.runeword;

import com.kevin.tyrrell.diablo.diablo.item.ItemType;
import com.kevin.tyrrell.diablo.diablo.rune.ReadOnlyRuneMap;
import com.kevin.tyrrell.diablo.util.Queryable;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines a storage unit for Runewords which offers
 * the capability to filter subsets of Runewords.
 *
 * @since 3.0
 */
public final class RunewordFilter
{
    /* Runes in which the user currently owns. */
    private final ReadOnlyRuneMap userRunes;

    /* Item types in which the user wishes to ignore. */
    private final Set<ItemType> ignoredTypes = EnumSet.noneOf(ItemType.class);
    private final Set<Runeword> ignoredWords = new HashSet<>();

    /* Current sorting preference. */
    private Sort currentSort = Sort.BY_RARITY;

    /**
     * Constructs a runeword filter.
     * The filter must be provided the loaded set of runewords.
     *
     * @param loader Loaded runewords from the storage medium.
     */
    public RunewordFilter(final RunewordLoader loader, final ReadOnlyRuneMap userRunes)
    {
        this.userRunes = requireNonNull(userRunes);
    }


}
