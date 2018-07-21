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
 * File Name:       ItemType
 * File Author:     Kevin Tyrrell
 * Date Created:    03/11/2018
 */

/**
 * Item types which Runewords can be placed into.
 * Some Item types such as gloves, belts, boots, etc
 * are omitted due to not being able to have sockets.
 * 
 * Note: Orbs can actually have Runewords placed in them.
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

    /**
     * @return String representation of the Item type.
     */
    @Override public String toString()
    {
        return name;
    }
}
