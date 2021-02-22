package com.kevin.tyrrell.diablo.diablo.rune;

import com.kevin.tyrrell.diablo.diablo.item.ItemType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static com.kevin.tyrrell.diablo.diablo.item.ItemType.*;

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
}
