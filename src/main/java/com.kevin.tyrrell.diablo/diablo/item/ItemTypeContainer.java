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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines a foundation for hierarchy among item types.
 *
 * @since 2.0
 */
public enum ItemTypeContainer
{
    SHIELD,
    WEAPON,
    MELEE_WEAPON(WEAPON),
    MISSILE_WEAPON(WEAPON);

    /* Item types inside the container (cyclic linking if EnumSet). */
    private final Set<ItemType> types = new HashSet<>();
    /* Container which includes this sub-container. */
    private final ItemTypeContainer parent;

    ItemTypeContainer(final ItemTypeContainer parent)
    {
        this.parent = parent;
    }

    ItemTypeContainer()
    {
        parent = null;
    }

    /**
     * Recursive method which allows containers to have hierarchy.
     * If an Item type container has a parent, any added types to
     * this container are also reflected in the parent.
     * @param type Item type to add to the container.
     */
    void addType(final ItemType type)
    {
        assert type != null;
        // Recursively add types to parent enum.
        if (types.add(type) && parent != null)
            parent.addType(type);
    }

    /**
     * Retrieves all values associated with this enum and its children.
     * @return set of all ItemTypes of this enum class, including children classes.
     */
    public Set<ItemType> getValues()
    {
        return Collections.unmodifiableSet(types);
    }
}
