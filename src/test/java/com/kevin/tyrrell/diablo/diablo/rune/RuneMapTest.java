package com.kevin.tyrrell.diablo.diablo.rune;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;
import static com.kevin.tyrrell.diablo.diablo.rune.Rune.*;

/**
 * ...
 *
 * @since 1.0
 */
public class RuneMapTest
{
    private RuneMap r1, r2;

    @Before
    public void setUp() throws Exception
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
}