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

    /**
     * Constructs an empty rune map.
     */
    public RuneMap()
    {
        runeCount = new CachedValue<>()
        {
            @Override protected Map<Rune, Integer> recalculate(Map<Rune, Integer> oldValue)
            {
                return new EnumMap<>(Rune.class);
            }
        };

        readOnlyRC = new CachedValue<>()
        {
            @Override protected Map<Rune, Integer> recalculate(Map<Rune, Integer> oldValue)
            {
                return Collections.unmodifiableMap(runeCount.get());
            }
        };
    }

    // TODO: Make a method for assessment of a rune map
    // TODO: Make a comparison method that compares two rune maps
    // TODO: This comparison is what will dictate runeword completion.

    /**
     * Moves a number of a specified rune into or out of the rune map.
     *
     * @param key Rune to move.
     * @param diff Number of runes to add or remove.
     */
    public void moveRunes(final Rune key, final int diff)
    {
        runeCount.get().merge(requireNonNull(key), diff, RuneMap::runeRemapper);
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
    }

    public void addAll(final Stream<Rune> stream)
    {
        final Map<Rune, Integer> rc = runeCount.get();
        requireNonNull(stream)
                .forEach(r -> rc.merge(r, 1, RuneMap::runeRemapper));
    }

    /**
     * @return Read-only view of the rune map.
     */
    public Map<Rune, Integer> getRunes()
    {
        return readOnlyRC.get();
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
