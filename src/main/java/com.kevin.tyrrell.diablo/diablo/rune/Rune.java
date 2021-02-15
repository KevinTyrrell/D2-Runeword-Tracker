/*
 * Application which tracks Runeword progress in the video game Diablo 2.
 * Copyright (C) 2018  Kevin Tyrrell
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

package com.kevin.tyrrell.diablo.diablo.rune;

import com.kevin.tyrrell.diablo.util.EnumExtendable;

import java.util.List;
import java.util.Map;

/**
 * Defines all 26 Runes in Diabo 2, along with related drop rates.
 *
 * @since 2.0
 */
public enum Rune implements EnumExtendable<Rune>
{
    /* Drop Source: https://diablo2.diablowiki.net/Guide:Rune_Finder_Guide_v1.10,_by_Urlik */
    EL("El", 215493),
    ELD("Eld", 143662),
    TIR("Tir", 119718),
    NEF("Nef", 79812),
    ETH("Eth", 83803),
    ITH("Ith", 55868),
    TAL("Tal", 69836),
    RAL("Ral", 46557),
    ORT("Ort", 48885),
    THUL("Thul", 32590),
    AMN("Amn", 29874),
    SOL("Sol", 19916),
    SHAEL("Shael", 15767),
    DOL("Dol", 10511),
    HEL("Hel", 8102),
    IO("Io", 5402),
    LUM("Lum", 4107),
    KO("Ko", 2738),
    FAL("Fal", 2068),
    LEM("Lem", 1379),
    PUL("Pul", 1038),
    UM("Um", 692),
    MAL("Mal", 594),
    IST("Ist", 396),
    GUL("Gul", 340),
    VEX("Vex", 226),
    OHM("Ohm", 194),
    LO("Lo", 129),
    SUR("Sur", 111),
    BER("Ber", 74),
    JAH("Jah", 63),
    CHAM("Cham", 42),
    ZOD("Zod", 12);

    /* Name of the rune. */
    private final String name;
    /* Chance for the Rune to drop out of a sample size of 1,000,000. */
    private final double rarity;

    /* Number of drops in the sample size. @see: Rune#getRarity() */
    private static final int DROP_SAMPLE_SIZE = 1000000;

    /**
     * Extension of the enum, adding additional functionality.
     */
    public static final EnumExtendable<Rune> ext = new EnumExtendable<>()
    {
        private final List<Rune> values = EnumExtendable.createEnumList(Rune.values());
        private final Map<String, Rune> stringMap = EnumExtendable.createStringMap(
                values, rune -> rune.toString().toLowerCase());

        @Override public List<Rune> values() { return values; }
        @Override public Map<String, Rune> stringMap() { return stringMap; }
    };
    
    /**
     * @param name Name of the rune.
     * @param dropValue Number of drop occurrences in the sample size.
     */
    Rune(final String name, final int dropValue)
    {
        assert name != null;
        assert dropValue > 0;
        this.name = name;
        rarity = (double)dropValue / DROP_SAMPLE_SIZE;
    }

    /**
     * Queries the tier of the rune.
     *
     * The 33 runes are divided into three tiers: low, mid, high.
     * Low: El -> Amn
     * Mid: Sol -> Um
     * High: Mal -> Zod
     *
     * @return -1 for low, 0 for mid, 1 for high.
     */
    public int getTier()
    {
        return (int)((double)ordinal() / ext.size() * 3) - 1;
    }

    /**
     * @return Name of the Rune.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Retrieves the chance for the Rune to drop, on average.
     *
     * Sample size: 1,000,000 rune drops.
     * Rune drop chances are based off of data provided by 1.10 user Urlik.
     * Source: https://diablo2.diablowiki.net/Guide:Rune_Finder_Guide_v1.10,_by_Urlik
     */
    public double getRarity()
    {
        return rarity;
    }

    /**
     * @return String representation of the Rune.
     */
    @Override public String toString()
    {
        return name;
    }
}
