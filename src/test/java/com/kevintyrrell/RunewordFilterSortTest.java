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
import com.kevintyrrell.model.diablo.runeword.RunewordLoader;
import com.kevintyrrell.model.diablo.runeword.RunewordSorter;
import com.kevintyrrell.model.diablo.runeword.Runeword;
import com.kevintyrrell.model.diablo.runeword.RunewordFilter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * JUnit testing class.
 *
 * @since 3.0
 */
public class RunewordFilterSortTest
{
    private RuneMap runes;
    private RunewordLoader loader;
    private RunewordFilter filter;
    private RunewordSorter sorter;
    private List<Runeword> sorted;

    public void sort()
    {
        sorted = sorter.flatMap(filter.stream())
                .collect(Collectors.toList());
    }

    @Before @Test public void setup()
    {
        runes = new RuneMap();
        loader = new RunewordLoader();
        filter = new RunewordFilter(new ArrayList<>(loader.stringMap().values()), runes);
        sorter = new RunewordSorter(runes);
    }

    @Test public void runewordFilterSortTest1()
    {
        final String[] rwNames = { "ancients_pledge", "black", "fury", "holy_thunder", "honor", "kings_grace", "leaf", "lionheart", "lore", "malice", "melody", "memory", "nadir", "radiance", "rhyme", "silence", "smoke", "stealth", "steel", "strength", "venom", "wealth", "white", "zephyr", "beast", "bramble", "breath_of_the_dying", "call_to_arms", "chains_of_honor", "chaos", "crescent_moon", "delirium", "doom", "duress", "enigma", "eternity", "exile", "famine", "gloom", "hand_of_justice", "heart_of_the_oak", "kingslayer", "passion", "prudence", "sanctuary", "splendor", "stone", "wind", "brand", "death", "destruction", "dragon", "dream", "edge", "faith", "fortitude", "grief", "harmony", "ice", "infinity", "insight", "last_wish", "lawbringer", "oath", "obedience", "phoenix", "pride", "rift", "spirit", "voice_of_reason", "wrath", "bone", "enlightenment", "myth", "peace", "principle", "rain", "treachery" };
        assertEquals(78, rwNames.length); // there are 78 runewords.
        Arrays.stream(rwNames).forEach(name ->
        {
            System.out.println("Attempting to load Runeword: " + name);
            assertNotNull(loader.fromString(name));
        });
    }

    @Test public void runewordFilterSortTest2()
    {
        runes.addRunes(Stream.of(Rune.JAH, Rune.ITH, Rune.BER));
        assertEquals(1.0, runes.progressTowards(loader.fromString("enigma")), 0.0);
    }

    @Test public void runewordFilterSortTest3()
    {
        runes.addRunes(Rune.ZOD, 1);
        assertTrue(runes.progressTowards(loader.fromString("breath_of_the_dying")) > 0.40);
        assertEquals(0.0, runes.progressTowards(loader.fromString("stealth")), 0.0);
    }

    @Test public void runewordFilterSortTest4()
    {
        runes.addRunes(Stream.of(Rune.JAH, Rune.JAH, Rune.BER, Rune.BER));
        sorter.sortBy(RunewordSorter.Sort.BY_PROGRESS);
        sort();
        assertEquals(loader.fromString("enigma"), sorted.get(sorted.size() - 1));
    }

    @Test public void runewordFilterSortTest5()
    {
        sort();
        assertEquals(0, sorted.size());
    }

    @Test public void runewordFilterSortTest6()
    {
        runes.addRunes(Stream.of(Rune.SOL, Rune.UM));
        final double p1 = runes.progressTowards(loader.fromString("crescent_moon"));
        assertTrue(p1 >= 0.95f);
        runes.addRunes(Rune.UM, 1);
        final double p2 = runes.progressTowards(loader.fromString("crescent_moon"));
        assertEquals(p1, p2, 0.0);

        sort();
        assertTrue(sorted.size() < loader.stringMap().size());
        filter.setProgressThreshold(0.98f);
        sort();
        assertEquals(1, sorted.size());
        assertEquals(loader.fromString("bone"), sorted.get(0));
    }

    @Test public void runewordFilterSortTest7()
    {
        runes.addRunes(Stream.of(Rune.ORT, Rune.RAL, Rune.TAL, Rune.UM));
        sorter.sortBy(RunewordSorter.Sort.BY_NAME);
        sort();
        assertEquals(loader.fromString("ancients_pledge"), sorted.get(0));
        sorter.sortBy(RunewordSorter.Sort.BY_RARITY);
        sort();
        assertEquals(loader.fromString("chaos"), sorted.get(sorted.size() - 1));
        sorter.sortBy(RunewordSorter.Sort.BY_PROGRESS);
        sort();
        assertEquals(loader.fromString("ancients_pledge"), sorted.get(sorted.size() - 1));
        sorter.sortBy(RunewordSorter.Sort.BY_LEVEL);
        sort();
        assertEquals(loader.fromString("stealth"), sorted.get(0));
        sorter.sortBy(RunewordSorter.Sort.BY_SOCKETS);
        sort();
        assertEquals(loader.fromString("stealth"), sorted.get(0));
        assertEquals(loader.fromString("kingslayer"), sorted.get(sorted.size() - 1));
        filter.setProgressThreshold(0);
        sort();
        assertEquals(loader.fromString("breath_of_the_dying"), sorted.get(sorted.size() - 1));
    }
}
