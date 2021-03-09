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

import com.kevintyrrell.model.diablo.item.ItemType;
import org.junit.Test;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * JUnit testing class.
 *
 * @since 3.0
 */
public class ItemTypeTest
{
    @Test public void itemTypeTest1()
    {
        final EnumSet<ItemType> a = EnumSet.of(ItemType.AXE, ItemType.CLUB, ItemType.CLAW, ItemType.HAMMER, ItemType.MACE, ItemType.POLEARM, ItemType.SCEPTER, ItemType.STAFF, ItemType.SWORD, ItemType.WAND);

        final List<ItemType> types = Collections.singletonList(ItemType.MELEE);
        final EnumSet<ItemType> b = types.stream()
                .flatMap(t ->
                        t.getChildren() == null ? Stream.of(t) : t.getChildren().stream())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(ItemType.class)));
        assertEquals(a, b);
    }

    @Test public void itemTypeTest2()
    {
        assertFalse(ItemType.WEAPON.getChildren().contains(ItemType.MELEE));
        assertFalse(ItemType.WEAPON.getChildren().contains(ItemType.MISSILE));
        assertTrue(ItemType.WEAPON.getChildren().contains(ItemType.AXE));
        assertTrue(ItemType.SHIELD.getChildren().contains(ItemType.AURIC));
        assertFalse(ItemType.MELEE.getChildren().contains(ItemType.MELEE));
    }
}
