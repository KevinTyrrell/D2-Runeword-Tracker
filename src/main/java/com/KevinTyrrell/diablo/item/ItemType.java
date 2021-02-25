/*
 *     TODO: ...
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

package com.KevinTyrrell.diablo.item;

import com.KevinTyrrell.util.EnumExtendable;

import java.util.*;
import java.util.stream.Stream;

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
    SHIELD(4),
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
    ORB(3), // There are no valid Runewords for Orbs (verify?)
    POLEARM(MELEE, 6),
    SCEPTER(MELEE, 5),
    STAFF(MELEE, 6),
    SWORD(MELEE, 6),
    WAND(MELEE, 2);

    /* Name of the Item type. */
    private final String name;
    /* Max amount of sockets the Item type can have. */
    private int sockets;
    /* Item types which encompass other item types. */
    private Set<ItemType> children;

    /**
     * Extension of the enum, adding additional functionality.
     */
    public static final EnumExtendable<ItemType> extension = new EnumExtendable<>(ItemType.class);

    static // Tree currently only tracks parents. Reverse direction to only track children.
    {
        /* Setup all parental relationships -- EnumMap is extremely low overhead. */
        final EnumMap<ItemType, ItemType> parentMap = new EnumMap<>(ItemType.class);
        final EnumMap<ItemType, Set<ItemType>> childrenMap = new EnumMap<>(ItemType.class);
        for (final ItemType child : extension.values())
        {
            if (child.children == null) continue;
            final ItemType parent = child.children.iterator().next(); // constructor guarantees safety.
            parentMap.put(child, parent); // track parent so we don't need to use iterator anymore.
            Set<ItemType> children = childrenMap.get(parent);
            if (children == null)
            {
                children = EnumSet.noneOf(ItemType.class);
                childrenMap.put(parent, children);
            }

            /* Reject children that are containers. */
            if (child.isConcrete()) children.add(child);
        }
        parentMap.keySet().forEach(k -> k.children = null); // Bereave all parents.

        /* Add all distant ancestors to each parent into their children collection. */
        for (final Map.Entry<ItemType, ItemType> itEntry : parentMap.entrySet())
        {
            ItemType child = itEntry.getValue(), parent = parentMap.get(child);
            child.sockets = Math.max(child.sockets, itEntry.getKey().sockets);

            while (parent != null)
            {
                /* It shouldn't be possible for the map calls to fail. */
                final Set<ItemType> children = childrenMap.get(parent);
                /* Avoid adding any of the container types. */
                childrenMap.get(child).stream()
                        .filter(ItemType::isConcrete)
                        .forEach(children::add);
                child = parent; parent = parentMap.get(parent); // Iterate upwards.
            }
        }
        /* Ensure all children all read-only. */
        childrenMap.forEach((key, value) -> key.children = Collections.unmodifiableSet(value));
    }

    /* Constructs a root-level item-type. */
    ItemType() { this(null, -1); }
    ItemType(final ItemType parent) { this(parent, -1); }
    ItemType(final int sockets) { this(null, sockets); }

    ItemType(final ItemType parent, final int sockets)
    {
        name = EnumExtendable.formalName(this);
        this.sockets = sockets;
        if (parent != null) // Temporarily save parent reference without having an instance variable.
            children = Collections.singletonMap(parent, null).keySet();
    }

    /**
     * @return Children of the item type.
     */
    public Set<ItemType> getChildren()
    {
        return children;
    }

    /**
     * Creates a new stream of the item type and all of its children.
     *
     * If the item type being included in the returned
     * stream is undesired, call #getChildren() instead.
     *
     * @return Stream of the item type and all of its children.
     * @see #getChildren()
     */
    public Stream<ItemType> stream()
    {
        if (children == null) return Stream.of(this);
        if (!isConcrete()) return children.stream();
        return Stream.concat(children.stream(), Stream.of(this));
    }

    /**
     * Indicates whether this item type is a not a container of item types.
     *
     * @return true if the item type is not an item type container.
     */
    public boolean isConcrete()
    {
        return sockets >= 0;
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
    public int getSockets()
    {
        return sockets;
    }

    /**
     * @return String representation of the item type.
     */
    @Override public String toString()
    {
        return name;
    }
}
