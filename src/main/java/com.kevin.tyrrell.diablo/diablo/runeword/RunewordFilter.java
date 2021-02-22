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

    /**
     * Constructs a runeword filter.
     * The filter must be provided the loaded set of runewords.
     *
     * @param loader Loaded runewords from the storage medium.
     */
    public RunewordFilter(final RunewordLoader loader, final ReadOnlyRuneMap userRunes)
    {
        byCompletion = null;
        this.userRunes = requireNonNull(userRunes);
    }

    /* Cannot use enum here as we need a reference to the outer class. */
    public class Sort implements Queryable<Sort>
    {
        public final Sort BY_NAME = new Sort("Name", Comparator.comparing(Runeword::getName));
        public final Sort BY_RARITY = new Sort("Rarity", Comparator.comparingDouble(Runeword::appraise));
        public final Sort BY_LEVEL = new Sort("Level", BY_RARITY)
        {
            @Override public int initialCompare(final Runeword rw1, final Runeword rw2)
            {
                return Integer.compare(rw1.getLevel(), rw2.getLevel());
            }
        };
        public final Sort BY_SOCKETS = new Sort("Sockets", BY_LEVEL)
        {
            @Override public int initialCompare(final Runeword rw1, final Runeword rw2)
            {
                return Integer.compare(rw1.getRequiredSockets(), rw2.getRequiredSockets());
            }
        };
        public final Sort BY_PROGRESS = new Sort("Progress", BY_RARITY)
        {
            @Override public int initialCompare(final Runeword rw1, final Runeword rw2)
            {
                return Double.compare(userRunes.progressTowards(rw1), userRunes.progressTowards(rw2));
            }
        };

        /* Keyword associated to the sort. */
        private final String keyword;
        /* Single or multi-layer comparator. */
        private final Comparator<Runeword> compareCb;

        private final Map<String, Sort> stringMap = Queryable.createStringMap(
                Stream.of(BY_NAME, BY_RARITY, BY_LEVEL, BY_SOCKETS, BY_PROGRESS),
                sort -> sort.keyword.toLowerCase());

        /* Creates a single-layer sort. */
        public Sort(final String keyword, final Comparator<Runeword> compareCb)
        {
            assert keyword != null;
            assert !keyword.isEmpty();
            assert compareCb != null;
            this.keyword = keyword;
            this.compareCb = compareCb;
        }

        /* Constructs a sort with a fallback sort. */
        public Sort(final String keyword, final Sort fallback)
        {
            assert keyword != null;
            assert !keyword.isEmpty();
            assert fallback != null;
            this.keyword = keyword;
            compareCb = new Comparator<>()
            {
                @Override public int compare(final Runeword rw1, final Runeword rw2)
                {
                    /* If elements are equivalent, use a fallback to further sort them. */
                    final int cmp = initialCompare(rw1, rw2);
                    return cmp == 0 ? fallback.compareCb.compare(rw1, rw2) : cmp;
                }
            };
        }

        /* Method should be overridden for use with overloaded constructor. */
        public int initialCompare(final Runeword rw1, final Runeword rw2)
        {
            throw new UnsupportedOperationException();
        }

        /**
         * Read-only map which associates string representations
         * of objects to their respective constant object values.
         * <p>
         * By default, map keys are set to Object#toString(). This behavior
         * can be changed by overloading StringToValue#createStringMap().
         * <p>
         * This method should be implemented such that the returned map is
         * an instance variable of the class which is implementing this method.
         *
         * @return Read-only map associating string representations to object values.
         */
        @Override public Map<String, Sort> stringMap()
        {
            return stringMap;
        }
    }
}
