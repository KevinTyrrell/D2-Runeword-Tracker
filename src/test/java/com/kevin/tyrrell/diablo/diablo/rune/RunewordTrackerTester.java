package com.kevin.tyrrell.diablo.diablo.rune;

import com.kevin.tyrrell.diablo.diablo.item.ItemType;
import com.kevin.tyrrell.diablo.diablo.runeword.Runeword;
import com.kevin.tyrrell.diablo.diablo.runeword.RunewordFilter;
import com.kevin.tyrrell.diablo.diablo.runeword.RunewordLoader;
import com.kevin.tyrrell.diablo.diablo.runeword.RunewordSorter;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Filter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static com.kevin.tyrrell.diablo.diablo.item.ItemType.*;
import static com.kevin.tyrrell.diablo.diablo.rune.Rune.*;

/**
 * JUnit Testing Class
 *
 * @since 3.0
 */
public class RunewordTrackerTester
{
    @Test public void itemTypeTest1()
    {
        final EnumSet<ItemType> a = EnumSet.of(AXE, CLUB, CLAW, HAMMER, MACE, POLEARM, SCEPTER, STAFF, SWORD, WAND);

        final List<ItemType> types = Collections.singletonList(MELEE);
        final EnumSet<ItemType> b = types.stream()
                .flatMap(t ->
                        t.getChildren() == null ? Stream.of(t) : t.getChildren().stream())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(ItemType.class)));
        assertEquals(a, b);
    }

    @Test public void itemTypeTest2()
    {
        assertFalse(WEAPON.getChildren().contains(MELEE));
        assertFalse(WEAPON.getChildren().contains(MISSILE));
        assertTrue(WEAPON.getChildren().contains(AXE));
        assertTrue(SHIELD.getChildren().contains(AURIC));
        assertFalse(MELEE.getChildren().contains(MELEE));
    }

    @Test public void runewordLoaderTest1()
    {
        RunewordLoader loader = new RunewordLoader();
        assertTrue(true);
    }

    @Test public void runewordLoaderTest2()
    {
        final RunewordLoader loader = new RunewordLoader();
        final String[] rwNames = { "ancients_pledge", "black", "fury", "holy_thunder", "honor", "kings_grace", "leaf", "lionheart", "lore", "malice", "melody", "memory", "nadir", "radiance", "rhyme", "silence", "smoke", "stealth", "steel", "strength", "venom", "wealth", "white", "zephyr", "beast", "bramble", "breath_of_the_dying", "call_to_arms", "chains_of_honor", "chaos", "crescent_moon", "delirium", "doom", "duress", "enigma", "eternity", "exile", "famine", "gloom", "hand_of_justice", "heart_of_the_oak", "kingslayer", "passion", "prudence", "sanctuary", "splendor", "stone", "wind", "brand", "death", "destruction", "dragon", "dream", "edge", "faith", "fortitude", "grief", "harmony", "ice", "infinity", "insight", "last_wish", "lawbringer", "oath", "obedience", "phoenix", "pride", "rift", "spirit", "voice_of_reason", "wrath", "bone", "enlightenment", "myth", "peace", "principle", "rain", "treachery" };
        assertEquals(78, rwNames.length); // there are 78 runewords.
        Arrays.stream(rwNames).forEach(name -> {
            System.out.println("Attempting to load Runeword: " + name);
            assertNotNull(loader.fromString(name));
        });
    }

    @Test public void runewordFilterSortTest1()
    {
        RuneMap runes = new RuneMap(Stream.of(JAH, ITH, BER));
        RunewordLoader loader = new RunewordLoader();
        assertEquals(1.0, runes.progressTowards(loader.fromString("enigma")), 0.0);
        runes = new RuneMap(Stream.of(ZOD));
        assertTrue(runes.progressTowards(loader.fromString("breath_of_the_dying")) > 0.40);
        assertEquals(0.0, runes.progressTowards(loader.fromString("stealth")), 0.0);

        runes = new RuneMap(Stream.of(JAH, JAH, BER, BER));
        RunewordFilter filter = new RunewordFilter(loader.stringMap().values());
        RunewordSorter sorter = new RunewordSorter(runes);
        sorter.sortBy(RunewordSorter.Sort.BY_PROGRESS);
        final List<Runeword> sorted = sorter.sort(filter.getRunewords())
                .collect(Collectors.toList());
        assertEquals(loader.fromString("enigma"), sorted.get(sorted.size() - 1));
    }
}
