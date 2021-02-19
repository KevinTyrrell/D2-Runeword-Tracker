package com.kevin.tyrrell.diablo.diablo.rune;

import com.kevin.tyrrell.diablo.diablo.item.ItemType;
import org.junit.Test;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static com.kevin.tyrrell.diablo.diablo.rune.Rune.*;
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
}
