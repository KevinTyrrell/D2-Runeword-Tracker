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

package com.kevintyrrell;

import com.kevintyrrell.model.diablo.rune.Rune;
import com.kevintyrrell.model.diablo.rune.RuneMap;
import com.kevintyrrell.model.diablo.runeword.Runeword;
import com.kevintyrrell.model.diablo.runeword.RunewordLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static com.kevintyrrell.model.diablo.rune.Rune.*;

/**
 * JUnit testing class.
 *
 * @since 3.0
 */
public class RuneMapTest
{
    private RuneMap r1, r2;

    @Before public void setUp() throws Exception
    {
        r1 = new RuneMap();
        r2 = new RuneMap();
    }

    @Test public void emptyTest1()
    {
        assertEquals(1.0, r1.progressTowards(r2), 1.0);
    }

    @Test public void emptyTest2()
    {
        r1.addRunes(RAL, 1);
        assertEquals(1.0, r1.progressTowards(r2), 1.0);
    }

    @Test public void emptyTest3()
    {
        r2.addRunes(RAL, 1);
        assertEquals(0.0, r1.progressTowards(r2), 0.0);
    }

    @Test public void progressTest1()
    {
        r1.addRunes(RAL, 1);
        r2.addRunes(RAL, 2);
        assertEquals(r1.progressTowards(r2), 0.5, 0.01);
    }

    @Test public void progressTest2()
    {
        r2.addRunes(Stream.of(RAL, TIR, TAL, SOL));
        r1.addRunes(Stream.of(RAL, TAL, SOL));
        assertEquals(0.80, r1.progressTowards(r2), 0.15);
    }

    @Test public void progressTest3()
    {
        r2.addRunes(Stream.of(VEX, HEL, EL, ELD, ZOD, ETH));
        r1.addRunes(Stream.of(VEX, HEL, ELD, ZOD, ETH));
        assertEquals(0.99, r1.progressTowards(r2), 0.01);
    }

    @Test public void progressTest4()
    {
        r2.addRunes(Stream.of(SOL, SOL, SOL, SOL));
        r1.addRunes(Stream.of(SOL, SOL, SOL, SOL, SOL, SOL, SOL, SOL, SOL, RAL, VEX, AMN));
        assertEquals(1.0, r1.progressTowards(r2), 0.0);
    }

    @Test public void tossableRunesTest1()
    {
        r1.addRunes(Stream.of(ETH, KO, KO, TIR, TIR, TIR, TIR));
        final RunewordLoader loader = new RunewordLoader();
        final Stream<Runeword> rws = Stream.of(
                loader.fromString("grief"), loader.fromString("leaf"), loader.fromString("strength"));
        final Map<Rune, Integer> expected = new EnumMap<>(Rune.class);
        expected.put(KO, 2);
        expected.put(TIR, 1);
        assertEquals(expected, r1.tossableRunes(rws));
    }

    @Test public void tossableRunesTest2()
    {
        r1.addRunes(Stream.of(HEL, HEL, BER, OHM, TAL, AMN));
        final RunewordLoader loader = new RunewordLoader();
        final Stream<Runeword> rws = Stream.of(loader.fromString("stealth"));
        final Map<Rune, Integer> expected = new EnumMap<>(Rune.class);
        expected.put(AMN, 1);
        assertEquals(expected, r1.tossableRunes(rws));
    }
}