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

package com.kevintyrrell.model.diablo.rune;

import com.kevintyrrell.model.diablo.runeword.Runeword;
import com.kevintyrrell.model.diablo.runeword.RunewordFilter;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines a read-only reference for rune maps.
 *
 * @since 3.0
 */
public interface ReadOnlyRuneMap
{
    /**
     * @return Read-only view of the rune map.
     */
    Map<Rune, Integer> getRunes();

    /**
     * Appraises the rarity of a rune map.
     *
     * Appraisal is directly correlated to the rarity
     * of the runes and their respective quantities.
     *
     * @return Appraisal of the rune map.
     */
    double appraise();

    /**
     * Evaluates the progress towards collecting all runes from another rune map.
     *
     * Completion of 0 indicates neither map have any runes in common, while a
     * completion of 1 indicates the map at least has all of the other maps runes.
     * Having a higher quantity of an in-common rune will not add to completion.
     *
     * @param other Other rune map to compare to.
     * @return Progress towards matching another rune map, from [0, 1].
     */
    default double progressTowards(final ReadOnlyRuneMap other)
    {
        final Map<Rune, Integer> a = requireNonNull(getRunes()), b = requireNonNull(other).getRunes();
        if (b.isEmpty()) return 1; // Divide by zero protection.
        return a.keySet().stream()
                .filter(b::containsKey) // Check only runes in common.
                /* Invert the rarity and multiply by at MOST the quantity they have in common. */
                .mapToDouble(r -> (1 / r.getRarity()) * (Math.min(a.get(r), b.get(r))))
                .sum() / other.appraise();
    }

    /**
     * Calculates which runes in the rune map, if any, are not actively
     * furthering completion of the any of the specified runewords.
     *
     * An example call would produce the following tossable runes:
     *  Rune map: { Eth x1, Ko x2, Tir x4 }
     *  Runewords: { Grief, Leaf, Strength }
     *  Tossable: { Ko x2, Tir x1 }
     *  Eth x1/Tir x1 governs Grief, Tir x1 governs Leaf, & Tir x1 governs Strength,
     *  leaving Ko x2 & Tir x1 as runes which may be tossed by the user without impact.
     *
     * High runes and Hel runes are never considered tossable due to their inherent value.
     *
     * For more practical results, it is recommended that the runewords be
     * filtered before calling this method. Otherwise, the vast majority of
     * runes will be advised to be kept, due to unwanted runewords reserving them.
     *
     * For example, a Rune map of { El x1 } is not likely reserving said Rune for "Breath of the Dying".
     *
     * This implementation could achieve faster speeds through access to a Map[Rune, Runeword],
     * where each Rune is associated to all Runewords which include it. However the structure of
     * this program does not provide all known Runewords at compile-time. Therefore it is not
     * within reason to pre-calculate said map at runtime just to improve the speed of this method.
     *
     * @param runewords Runewords to evaluate.
     * @return Map of runes and their quantities which may be tossed without impact.
     * @see RunewordFilter#stream()
     */
    default Map<Rune, Integer> tossableRunes(final Stream<Runeword> runewords)
    {
        /* Duplicate the current runes (fairly cheap operation due to enum map). */
        final Map<Rune, Integer> tossable = requireNonNull(getRunes()).entrySet().stream()
                /* Hel runes can be used for unsocketing, and are generally kept. */
                .filter(e -> e.getKey() != Rune.HEL)
                /* High runes under most circumstances should not be tossed. */
                .filter(e -> e.getKey().getTier() < 1)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        /* Integer.max to avoid object creation. */
                        Integer::max, () -> new EnumMap<>(Rune.class)));

        requireNonNull(runewords)
                .flatMap(runeword -> runeword.getRunes().entrySet().stream())
                .takeWhile(e -> !tossable.isEmpty())
                .forEach(e -> tossable.computeIfPresent(e.getKey(), (rune, count) ->
                        count <= e.getValue() ? null : count - e.getValue()));
        return tossable;
    }
}
