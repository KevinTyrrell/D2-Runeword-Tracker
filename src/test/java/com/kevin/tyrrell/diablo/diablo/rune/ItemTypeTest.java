/*
 * Application which tracks Runeword progress in the video game Diablo 2.
 * Copyright (C) 2021  Kevin Tyrrell
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

import com.kevin.tyrrell.diablo.diablo.item.ItemType;
import org.junit.Test;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static com.kevin.tyrrell.diablo.diablo.item.ItemType.*;

/**
 * JUnit testing class.
 *
 * @since 3.0
 */
public class ItemTypeTest
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
}
