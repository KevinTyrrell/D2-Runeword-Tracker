package com.kevin.tyrrell.diablo.diablo.rune;

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
