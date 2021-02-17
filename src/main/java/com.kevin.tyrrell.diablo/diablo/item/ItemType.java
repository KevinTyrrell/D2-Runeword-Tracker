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

import com.kevin.tyrrell.diablo.util.EnumExtendable;

import java.util.*;

/**
 * Defines all possible item types in Diablo 2.
 *
 * @since 2.0
 */
public enum ItemType
{
    WEAPON,
    MELEE(WEAPON),
    MISSILE(WEAPON),
    SHIELD,
    AURIC(SHIELD, 4),
    AXE(MELEE, 6),
    ARMOR(4),
    BOW(MISSILE, 6),
    CLAW(MELEE, 3),
    CLUB(MELEE, 3),
    CROSSBOW(MISSILE, 6),
    HAMMER(MELEE, 6),
    HELM(4),
    MACE(MELEE, 5),
    ORBS(3), // There are no valid Runewords for Orbs (verify?)
    POLEARM(MELEE, 6),
    SCEPTER(MELEE, 5),
    STAFF(MELEE, 6),
    SWORD(MELEE, 6),
    WAND(MELEE, 2);

    /* Max amount of sockets the Item type can have. */
    private int maxSockets;

    /* Name of the Item type. */
    private final String name;
    /* Item type which contains this type. */
    private final ItemType parent;
    /* Item types which encompass other item types. */
    private final EnumSet<ItemType> children;

    /**
     * Extension of the enum, adding additional functionality.
     */
    public static final EnumExtendable<ItemType> extension = new EnumExtendable<>(ItemType.class);

    /* Constructs a root-level item-type. */
    ItemType()
    {
        this(null, EnumSet.noneOf(ItemType.class), -1);
    }

    /* Constructs a parent-level item-type. */
    ItemType(final ItemType parent)
    {
        this(parent, EnumSet.noneOf(ItemType.class), -1);
        parent.addType(this);
    }

    /* Constructs a child-level item-type. */
    ItemType(final ItemType parent, final int maxSockets)
    {
        this(parent, null, maxSockets);
        assert maxSockets > 0;
        assert parent != null;
        parent.addType(this, maxSockets);
    }

    /* Constructs a child-level item-type. */
    ItemType(final int maxSockets)
    {
        this(null, null, maxSockets);
        assert maxSockets > 0;
    }

    /* Shared constructor. */
    ItemType(final ItemType parent, final EnumSet<ItemType> children, final int maxSockets)
    {
        this.parent = parent;
        this.children = children;
        this.maxSockets = maxSockets;
        name = EnumExtendable.formalName(this);
    }

    /* Add item type as a child, recursively upwards. */
    private void addType(final ItemType type)
    {
        assert type != null;
        assert children != null;
        if (children.add(type) && parent != null)
            parent.addType(this);
    }

    /* Used when checking for an increase of sockets. */
    private void addType(final ItemType type, final int maxSockets)
    {
        assert type != null;
        assert maxSockets > 0;
        assert children != null;
        if (!children.add(type)) return;
        if (this.maxSockets < maxSockets)
        {
            /* Inform parent to check max sockets. */
            this.maxSockets = maxSockets;
            parent.addType(this, maxSockets);
        }
        else if (parent != null)
            parent.addType(this);
    }

    /**
     * @return Children of the item type.
     */
    public Set<ItemType> getChildren()
    {
        return children != null ? Collections.unmodifiableSet(children) : null;
    }

    /**
     * @return Name of the item type.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return Maximum sockets bases the item type can have.
     */
    public int getMaxSockets()
    {
        return maxSockets;
    }

    /**
     * @return String representation of the item type.
     */
    @Override public String toString()
    {
        return name;
    }
}
