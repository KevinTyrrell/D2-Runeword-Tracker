/*
Copyright © 2018 Kevin Tyrrell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package diablo.item;

/*
 * File Name:       ItemTypeContainer
 * File Author:     Kevin Tyrrell
 * Date Created:    03/11/2018
 */

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Describes a container that holds multiple Item types.
 * This is needed as certain Runewords may specify multiple
 * Item types in a grouping such as "ALL WEAPONS". Thus here,
 * there is a hierarchy of containers which hold those Item types.
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
