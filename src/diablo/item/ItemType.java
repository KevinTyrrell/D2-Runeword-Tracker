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

public enum ItemType
{
    SHIELD("Shield", ItemTypeContainer.SHIELD),
    AURIC_SHIELD("Auric Shield", ItemTypeContainer.SHIELD),
    CLUB("Club", ItemTypeContainer.MELEE_WEAPON),
    HAMMER("Hammer", ItemTypeContainer.MELEE_WEAPON),
    MACE("Mace", ItemTypeContainer.MELEE_WEAPON),
    SWORD("Sword", ItemTypeContainer.MELEE_WEAPON),
    AXE("Axe", ItemTypeContainer.MELEE_WEAPON),
    SCEPTER("Scepter", ItemTypeContainer.MELEE_WEAPON),
    STAFF("Staff", ItemTypeContainer.MELEE_WEAPON),
    ORB("Orb", ItemTypeContainer.MELEE_WEAPON),
    WAND("Wand", ItemTypeContainer.MELEE_WEAPON),
    CLAW("Claw", ItemTypeContainer.MELEE_WEAPON),
    POLEARM("Polearm", ItemTypeContainer.MELEE_WEAPON),
    BODY_ARMOR("Body Armor"),
    HELM("Helm"),
    BOW("Bow", ItemTypeContainer.MISSILE_WEAPON),
    CROSSBOW("Crossbow", ItemTypeContainer.MISSILE_WEAPON);

    private final String name;

    ItemType(final String name, final ItemTypeContainer container)
    {
        this(name);
        assert container != null;
        container.addType(this);
    }

    ItemType(final String name)
    {
        assert name != null;
        this.name = name;
    }

    @Override public String toString()
    {
        return name;
    }
}
