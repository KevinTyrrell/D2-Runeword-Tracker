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

package com.kevin.tyrrell.diablo.diablo.item;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Defines all possible item types in Diablo 2.
 *
 * @since 2.0
 */
public enum ItemType
{
    AURIC_SHIELD("Auric Shield", 4, ItemTypeContainer.SHIELD),
    AXE("Axe", 6, ItemTypeContainer.MELEE_WEAPON),
    BODY_ARMOR("Body Armor", 4),
    BOW("Bow", 6, ItemTypeContainer.MISSILE_WEAPON),
    CLAW("Claw", 3, ItemTypeContainer.MELEE_WEAPON),
    CLUB("Club", 3, ItemTypeContainer.MELEE_WEAPON),
    CROSSBOW("Crossbow", 6, ItemTypeContainer.MISSILE_WEAPON),
    HAMMER("Hammer", 6, ItemTypeContainer.MELEE_WEAPON),
    HELM("Helm", 4),
    MACE("Mace", 5, ItemTypeContainer.MELEE_WEAPON),
    /* Orbs are not considered as a melee or missile weapon. */
    ORB("Orb", 3),
    POLEARM("Polearm", 6, ItemTypeContainer.MELEE_WEAPON),
    SCEPTER("Scepter", 5, ItemTypeContainer.MELEE_WEAPON),
    SHIELD("Shield", 4, ItemTypeContainer.SHIELD),
    STAFF("Staff", 6, ItemTypeContainer.MELEE_WEAPON),
    SWORD("Sword", 6, ItemTypeContainer.MELEE_WEAPON),
    WAND("Wand", 2, ItemTypeContainer.MELEE_WEAPON);

    /* Name of the Item type. */
    private final String name;
    /* Max amount of sockets the Item type can have. */
    private final int maxSockets;

    ItemType(final String name, final int maxSockets, final ItemTypeContainer container)
    {
        this(name, maxSockets);
        assert container != null;
        container.addType(this);
    }

    ItemType(final String name, final int maxSockets)
    {
        assert name != null;
        assert maxSockets >= 0;
        this.name = name;
        this.maxSockets = maxSockets;
    }

    /**
     * @return Name of the Item type.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return Max amount of sockets that this Item type can have.
     */
    public int getMaxSockets()
    {
        return maxSockets;
    }

    private static final Map<String, ItemType> fromString = Arrays.stream(values())
            .collect(Collectors.collectingAndThen(
                    /* name() throws a 'cannot resolve name' in Intellij 2018, Java 9.0.4, but compiles. */
                    Collectors.toMap(k -> k.name().toLowerCase(), Function.identity()),
                    Collections::unmodifiableMap));

    /**
     * Looks up a Rune from a String.
     * @param str String to use for the lookup.
     * @return Rune corresponding to the String.
     */
    public static ItemType fromString(final String str)
    {
        assert str != null;
        return fromString.get(str);
    }

    /**
     * @return String representation of the Item type.
     */
    @Override public String toString()
    {
        return name;
    }
}
