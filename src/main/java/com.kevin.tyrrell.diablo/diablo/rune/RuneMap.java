package com.kevin.tyrrell.diablo.diablo.rune;

import com.kevin.tyrrell.diablo.util.CachedValue;
import com.kevin.tyrrell.diablo.util.Saveable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines an assortment of Runes.
 *
 * @since 3.0
 */
public class RuneMap implements ReadOnlyRuneMap, Saveable
{
    private final Map<Rune, Integer> runeCount;
    private transient final CachedValue<Map<Rune, Integer>> readOnlyRC;
    private transient final CachedValue<Double> appraisal;

    /**
     * Constructs an empty rune map.
     */
    public RuneMap()
    {
        this(new EnumMap<>(Rune.class));
    }

    public RuneMap(final Stream<Rune> stream)
    {
        this(requireNonNull(stream)
                .collect(Collectors.toMap(
                        Function.identity(), rune -> 1, Integer::sum,
                        () -> new EnumMap<>(Rune.class))));
    }

    /* Shared constructor. */
    private RuneMap(final Map<Rune, Integer> runes)
    {
        assert runes != null;
        runeCount = runes;

        readOnlyRC = new CachedValue<>()
        {
            @Override protected Map<Rune, Integer> recalculate()
            {
                return Collections.unmodifiableMap(runeCount);
            }
        };

        appraisal = new CachedValue<>()
        {
            @Override protected Double recalculate()
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
        runeCount.merge(requireNonNull(key), diff, RuneMap::runeRemapper);
        modifyFlags();
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
        runeCount.merge(requireNonNull(key), num, RuneMap::runeAddRemapper);
        modifyFlags();
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
        runeCount.merge(requireNonNull(key), num, RuneMap::runeTossRemapper);
        modifyFlags();
    }

    /**
     * Adds all of the runes in a stream into the rune map.
     *
     * @param stream Stream of runes.
     */
    public void addRunes(final Stream<Rune> stream)
    {
        requireNonNull(stream)
                .forEach(r -> runeCount.merge(r, 1, RuneMap::runeRemapper));
        modifyFlags();
    }

    /* Updates the necessary flags that the rune map has changed. */
    private void modifyFlags()
    {
        appraisal.invalidate();
        flagUnsavedChanges();
    }

    /**
     * @return Read-only view of the rune map.
     */
    @Override public Map<Rune, Integer> getRunes()
    {
        return readOnlyRC.get();
    }

    /**
     * Appraises the rarity of a rune map.
     * <p>
     * Appraisal is directly correlated to the rarity
     * of the runes and their respective quantities.
     *
     * @return Appraisal of the rune map.
     */
    @Override public double appraise()
    {
        return appraisal.get();
    }

    /* Helper method to enable caching. */
    protected static double appraiseRunes(final RuneMap runes)
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
