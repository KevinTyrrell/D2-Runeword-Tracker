package com.kevin.tyrrell.diablo.diablo.rune;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Defines an assortment of Runes.
 *
 * @since 3.0
 */
public class RuneMap implements Serializable
{
    // TODO: There is zero point in splitting up runes into high, low, med.
    // TODO: This is because by default the EnumMap sorts them by rarity.

    private final Map<Rune, Integer> s;





    private final Rune[] runes;
    private final int[] quantities;
}
