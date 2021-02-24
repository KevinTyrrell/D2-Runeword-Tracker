/*
 * Application which tracks Runeword progress in the video game Diablo 2.
 * Copyright (C) 2021  Kevin Tyrrell
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

package com.kevin.tyrrell.diablo.diablo.runeword;

import com.kevin.tyrrell.diablo.diablo.rune.ReadOnlyRuneMap;
import com.kevin.tyrrell.diablo.util.EnumExtendable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines a system for customizing how runewords are sorted.
 *
 * @since 3.0
 */
public class RunewordSorter
{
    /* Default sorting method. */
    private Sort currentSort = Sort.BY_RARITY;

    /* Associates each sort with its respective comparator. */
    private final Map<Sort, Comparator<Runeword>> comparatorMap;

    /**
     * @param userRunes Runes in which the player owns.
     */
    public RunewordSorter(final ReadOnlyRuneMap userRunes)
    {
        requireNonNull(userRunes);
        comparatorMap = Sort.extension.values().stream()
                .collect(Collectors.toMap(
                        Function.identity(), v -> v::cmp,
                        (rw1, rw2) -> { throw new RuntimeException(); },
                        () -> new EnumMap<>(Sort.class)));
        comparatorMap.put(Sort.BY_PROGRESS, (rw1, rw2) ->
        {
            /* This is semi-cached, as each rune map caches its appraisal. */
            final int cmp = Double.compare(userRunes.progressTowards(rw1), userRunes.progressTowards(rw2));
            /* Dive one layer deeper if comparison is equivalent. */
            if (cmp != 0 || Sort.BY_PROGRESS.nextLayer == null) return cmp;
            return Sort.BY_PROGRESS.nextLayer.cmp(rw1, rw2);
        });
    }

    /**
     * Sorts a subset of runewords by the current sort setting.
     *
     * @param runewords Runewords to be sorted.
     */
    public Stream<Runeword> sort(final Stream<Runeword> runewords)
    {
        return requireNonNull(runewords).sorted(comparatorMap.get(currentSort));
    }

    /**
     * @param sort Sorting setting to use.
     */
    public void sortBy(final Sort sort)
    {
        currentSort = requireNonNull(sort);
    }

    public enum Sort
    {
        BY_NAME("Name", Comparator.comparing(Runeword::getName)),
        BY_RARITY("Rarity", BY_NAME, Comparator.comparingDouble(Runeword::appraise)),
        BY_LEVEL("Level", BY_RARITY, Comparator.comparingInt(Runeword::getLevel)),
        BY_SOCKETS("Sockets", BY_LEVEL, Comparator.comparingInt(Runeword::getRequiredSockets)),
        /* By progress requires references to the outer class, which enum cannot access. */
        BY_PROGRESS("Progress", BY_RARITY, null);

        /* Sort which is to be used in-case of equivalent elements. */
        private final Comparator<Runeword> firstLayerCmp;
        private final Sort nextLayer;
        private final String name;

        public static final EnumExtendable<Sort> extension = new EnumExtendable<>(Sort.class)
        {
            @Override protected String stringMapKeyer(final Sort value)
            {
                return value.toString().toLowerCase();
            }
        };

        /* Constructs an un-usable sort. */
        Sort(final String name)
        {
            this(name, null, null);
        }

        /* Constructs a single-layer sort. */
        Sort(final String name, final Comparator<Runeword> firstLayerCmp)
        {
            this(name, null, firstLayerCmp);
        }

        /* Constructs a sort with multiple layers. */
        Sort(final String name, final Sort nextLayer, final Comparator<Runeword> firstLayerCmp)
        {
            assert name != null;
            assert !name.isEmpty();
            this.name = name;
            this.nextLayer = nextLayer;
            this.firstLayerCmp = firstLayerCmp;
        }

        /* We cannot use Comparable here, else other classes can just use Sort instead of RunewordSorter. */
        private int cmp(final Runeword rw1, final Runeword rw2)
        {
            assert rw1 != null;
            assert rw2 != null;
            if (rw1 == rw2) return 0;
            final int cmp = firstLayerCmp.compare(rw1, rw2);
            /* Check if the elements are equivalent at this layer. */
            if (cmp == 0 && nextLayer != null)
                /* Dive another layer down to break equivalency. */
                return nextLayer.cmp(rw1, rw2);
            return cmp;
        }

        /**
         * @return String representation of the sort.
         */
        @Override public String toString()
        {
            return name;
        }
    }
}
