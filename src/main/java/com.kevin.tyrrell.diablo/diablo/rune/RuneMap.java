package com.kevin.tyrrell.diablo.diablo.rune;

import com.kevin.tyrrell.diablo.util.CachedValue;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines an assortment of Runes.
 *
 * @since 3.0
 */
public class RuneMap implements Serializable
{
    private final CachedValue<Map<Rune, Integer>> runeCount;
    private transient final CachedValue<Map<Rune, Integer>> readOnlyRC;
    private transient final CachedValue<Double> appraisal;

    /**
     * Constructs an empty rune map.
     */
    public RuneMap()
    {
        runeCount = new CachedValue<>()
        {
            @Override protected Map<Rune, Integer> recalculate(final Map<Rune, Integer> oldValue)
            {
                return new EnumMap<>(Rune.class);
            }
        };

        readOnlyRC = new CachedValue<>()
        {
            @Override protected Map<Rune, Integer> recalculate(final Map<Rune, Integer> oldValue)
            {
                return Collections.unmodifiableMap(runeCount.get());
            }
        };

        appraisal = new CachedValue<>()
        {
            @Override protected Double recalculate(final Double oldValue)
            {
                return RuneMap.appraiseRunes(RuneMap.this);
            }
        };
    }

    /**
     * Moves a number of a specified rune into or out of the rune map.
     *
     * @param key Rune to move.
     * @param diff Number of runes to add or remove.
     */
    public void moveRunes(final Rune key, final int diff)
    {
        runeCount.get().merge(requireNonNull(key), diff, RuneMap::runeRemapper);
        appraisal.invalidate();
    }

    /**
     * Adds a specified number of a rune to the rune map.
     *
     * @param key Rune to add.
     * @param num Number of the specified rune to add.
     */
    public void addRunes(final Rune key, final int num)
    {
        if (num <= 0) throw new IllegalArgumentException("Number of runes must be positive.");
        runeCount.get().merge(requireNonNull(key), num, RuneMap::runeAddRemapper);
        appraisal.invalidate();
    }

    /**
     * Tosses out a specified number of a rune from the rune map.
     *
     * @param key Rune to toss.
     * @param num Number of the specified rune to toss.
     */
    public void tossRunes(final Rune key, final int num)
    {
        if (num <= 0) throw new IllegalArgumentException("Number of runes must be positive.");
        runeCount.get().merge(requireNonNull(key), num, RuneMap::runeTossRemapper);
        appraisal.invalidate();
    }

    /**
     * Adds all of the runes in a stream into the rune map.
     *
     * @param stream Stream of runes.
     */
    public void addRunes(final Stream<Rune> stream)
    {
        final Map<Rune, Integer> rc = runeCount.get();
        requireNonNull(stream)
                .forEach(r -> rc.merge(r, 1, RuneMap::runeRemapper));
        appraisal.invalidate();
    }

    /**
     * @return Read-only view of the rune map.
     */
    public Map<Rune, Integer> getRunes()
    {
        return readOnlyRC.get();
    }

    /**
     * Appraises the rarity of a rune map.
     *
     * Appraisal is directly correletated to the rarity
     * of the runes and their respective quantities.
     *
     * @return Appraisal of the rune map.
     */
    public double appraise()
    {
        return appraisal.get();
    }

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
    public double progressTowards(final RuneMap other)
    {
        final Map<Rune, Integer> a = runeCount.get(), b = requireNonNull(other).runeCount.get();
        if (b.isEmpty()) return 1; // Divide by zero protection.
        return runeCount.get().keySet().stream()
                .filter(b::containsKey) // Check only runes in common.
                /* Invert the rarity and multiply by at MOST the quantity they have in common. */
                .mapToDouble(r -> (1 / r.getRarity()) * (Math.min(a.get(r), b.get(r))))
                .sum() / other.appraisal.get();
    }

    /* Helper method to enable caching. */
    private static double appraiseRunes(final RuneMap runes)
    {
        assert runes != null;
        return runes.getRunes().entrySet().stream()
                .mapToDouble(e -> (1 / e.getKey().getRarity()) * e.getValue())
                .sum();
    }

    /* Callback function for adding runes. */
    private static Integer runeAddRemapper(final Integer oldValue, final Integer num)
    {
        assert oldValue > 0;
        assert num != null;
        assert num > 0;
        return oldValue + num;
    }

    /* Callback function for tossing runes. */
    private static Integer runeTossRemapper(final Integer oldValue, final Integer num)
    {
        assert oldValue > 0;
        assert num > 0;
        if (num > oldValue) throw new IllegalArgumentException("Total rune quantity cannot be negative.");
        return num < oldValue ? oldValue - num : null; // Remove entry if total is zero.
    }


    /* Callback function which modifies the quantity of a rune. */
    private static Integer runeRemapper(final Integer oldValue, final Integer diff)
    {
        assert oldValue > 0;
        assert diff != null;
        return diff > 0 ? runeAddRemapper(oldValue, diff) : runeTossRemapper(oldValue, diff);
    }
}
