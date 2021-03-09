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

import java.util.Map;

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
        final Map<Rune, Integer> a = getRunes(), b = requireNonNull(other).getRunes();
        if (b.isEmpty()) return 1; // Divide by zero protection.
        return a.keySet().stream()
                .filter(b::containsKey) // Check only runes in common.
                /* Invert the rarity and multiply by at MOST the quantity they have in common. */
                .mapToDouble(r -> (1 / r.getRarity()) * (Math.min(a.get(r), b.get(r))))
                .sum() / other.appraise();
    }
}
