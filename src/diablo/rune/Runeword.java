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

package diablo.rune;

/*
 * File Name:       Runeword
 * File Author:     Kevin Tyrrell
 * Date Created:    03/09/2018
 */

import diablo.item.ItemType;
import diablo.item.ItemTypeContainer;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static diablo.item.ItemType.*;
import static diablo.item.ItemType.BODY_ARMOR;
import static diablo.rune.Rune.*;
import static diablo.rune.Rune.LEM;

public enum Runeword
{
    /* Original Runewords */
    ANCIENTS_PLEDGE("Ancient's Pledge", ItemTypeContainer.SHIELD::getValues, RAL, ORT, TAL),
    BLACK("Black", () -> Stream.of(CLUB, HAMMER, MACE).collect(Collectors.toSet()), THUL, IO, NEF),
    FURY("Fury", ItemTypeContainer.MELEE_WEAPON::getValues, JAH, GUL, ETH),
    HOLY_THUNDER("Holy Thunder", () -> Stream.of(SCEPTER).collect(Collectors.toSet()), ETH, RAL, ORT, TAL),
    HONOR("Honor", ItemTypeContainer.MELEE_WEAPON::getValues, AMN, EL, ITH, TIR, SOL),
    KINGS_GRACE("King's Grace", () -> Stream.of(SWORD, SCEPTER).collect(Collectors.toSet()), AMN, RAL, THUL),
    LEAF("Leaf", () -> Stream.of(STAFF).collect(Collectors.toSet()), TIR, RAL),
    LIONHEART("Lionheart", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), HEL, LUM, FAL),
    LORE("Lore", () -> Stream.of(HELM).collect(Collectors.toSet()), ORT, SOL),
    MALICE("Malice", ItemTypeContainer.MELEE_WEAPON::getValues, ITH, EL, ETH),
    MELODY("Melody", ItemTypeContainer.MISSILE_WEAPON::getValues, SHAEL, KO, NEF),
    MEMORY("Memory", () -> Stream.of(STAFF).collect(Collectors.toSet()), LUM, IO, SOL, ETH),
    NADIR("Nadir", () -> Stream.of(HELM).collect(Collectors.toSet()), NEF, TIR),
    RADIANCE("Radiance", () -> Stream.of(HELM).collect(Collectors.toSet()), NEF, SOL, ITH),
    RHYME("Rhyme", ItemTypeContainer.SHIELD::getValues, SHAEL, ETH),
    SILENCE("Silence", ItemTypeContainer.WEAPON::getValues, DOL, ELD, HEL, IST, TIR, VEX),
    SMOKE("Smoke", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), NEF, LUM),
    STEALTH("Stealth", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), TAL, ETH),
    STEEL("Steel", () -> Stream.of(SWORD, AXE, MACE).collect(Collectors.toSet()), TIR, EL),
    STRENGTH("Strength", ItemTypeContainer.MELEE_WEAPON::getValues, AMN, TIR),
    VENOM("Venom", ItemTypeContainer.WEAPON::getValues, TAL, DOL, MAL),
    WEALTH("Wealth", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), LEM, KO, TIR),
    WHITE("White", () -> Stream.of(WAND).collect(Collectors.toSet()), DOL, IO),
    ZEPHYR("Zephyr", ItemTypeContainer.MISSILE_WEAPON::getValues, ORT, ETH),

    /* 1.10 Runewords */
    BEAST("Beast", () -> Stream.of(AXE, SCEPTER, HAMMER).collect(Collectors.toSet()), BER, TIR, UM, MAL, LUM),
    BRAMBLE("Bramble", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), RAL, OHM, SUR, ETH),
    BREATH_OF_THE_DYING("Breath of the Dying", ItemTypeContainer.WEAPON::getValues, VEX, HEL, EL, ELD, ZOD, ETH),
    CALL_TO_ARMS("Call to Arms", ItemTypeContainer.WEAPON::getValues, AMN, RAL, MAL, IST, OHM),
    CHAINS_OF_HONOR("Chains of Honor", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), DOL, UM, BER, IST),
    CHAOS("Chaos", () -> Stream.of(CLAW).collect(Collectors.toSet()), FAL, OHM, UM),
    CRESCENT_MOON("Crescent Moon", () -> Stream.of(AXE, SWORD, POLEARM).collect(Collectors.toSet()), SHAEL, UM, TIR),
    DELIRIUM("Delirium", () -> Stream.of(HELM).collect(Collectors.toSet()), LEM, IST, KO),
    DOOM("Doom", () -> Stream.of(AXE, POLEARM, HAMMER).collect(Collectors.toSet()), HEL, OHM, UM, LO, CHAM),
    DURESS("Duress", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), SHAEL, UM, THUL),
    ENIGMA("Engima", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), JAH, ITH, BER),
    ETERNITY("Eternity", ItemTypeContainer.MELEE_WEAPON::getValues, AMN, BER, IST, SOL, SUR),
    EXILE("Exile", () -> Stream.of(AURIC_SHIELD).collect(Collectors.toSet()), VEX, OHM, IST, DOL),
    FAMINE("Famine", () -> Stream.of(AXE, HAMMER).collect(Collectors.toSet()), FAL, OHM, ORT, JAH),
    GLOOM("Gloom", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), FAL, UM, PUL),
    HAND_OF_JUSTICE("Hand of Justice", ItemTypeContainer.WEAPON::getValues, SUR, CHAM, AMN, LO),
    HEART_OF_THE_OAK("Heart of the Oak", () -> Stream.of(STAFF, MACE).collect(Collectors.toSet()), KO, VEX, PUL, THUL),
    KINGSLAYER("Kingslayer", () -> Stream.of(SWORD, AXE).collect(Collectors.toSet()), MAL, UM, GUL, FAL),
    PASSION("Passion", ItemTypeContainer.WEAPON::getValues, DOL, ORT, ELD, LEM),
    PRUDENCE("Prudence", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), MAL, TIR),
    SANCTUARY("Sanctuary", ItemTypeContainer.SHIELD::getValues, KO, KO, MAL),
    SPLENDOR("Splendor", ItemTypeContainer.SHIELD::getValues, ETH, LUM),
    STONE("Stone", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), SHAEL, UM, PUL, LUM),
    WIND("Wind", ItemTypeContainer.MELEE_WEAPON::getValues, SUR, EL),

    /* 1.10 Runewords (Ladder Only) */
    BRAND("Brand", ItemTypeContainer.MISSILE_WEAPON::getValues, JAH, LO, MAL, GUL),
    DEATH("Death", () -> Stream.of(SWORD, AXE).collect(Collectors.toSet()), HEL, EL, VEX, ORT, GUL),
    DESTRUCTION("Destruction", () -> Stream.of(POLEARM, SWORD).collect(Collectors.toSet()), VEX, LO, BER, JAH, KO),
    DRAGON("Dragon", () -> Stream.concat(ItemTypeContainer.SHIELD.getValues().stream(),
            Stream.of(BODY_ARMOR)).collect(Collectors.toSet()), SUR, LO, SOL),
    DREAM("Dream", () -> Stream.concat(ItemTypeContainer.SHIELD.getValues().stream(),
            Stream.of(HELM)).collect(Collectors.toSet()), IO, JAH, PUL),
    EDGE("Edge", ItemTypeContainer.MISSILE_WEAPON::getValues, TIR, TAL, AMN),
    FAITH("Faith", ItemTypeContainer.MISSILE_WEAPON::getValues, OHM, JAH, LEM, ELD),
    FORTITUDE("Fortitude", () -> Stream.concat(ItemTypeContainer.WEAPON.getValues().stream(),
            Stream.of(BODY_ARMOR)).collect(Collectors.toSet()), EL, SOL, DOL, LO),
    GRIEF("Grief", () -> Stream.of(SWORD, AXE).collect(Collectors.toSet()), ETH, TIR, LO, MAL, RAL),
    HARMONY("Harmony", ItemTypeContainer.MISSILE_WEAPON::getValues, TIR, ITH, SOL, KO),
    ICE("Ice", ItemTypeContainer.MISSILE_WEAPON::getValues, AMN, SHAEL, JAH, LO),
    INFINITY("Infinity", () -> Stream.of(POLEARM).collect(Collectors.toSet()), BER, MAL, BER, IST),
    INSIGHT("Insight", () -> Stream.of(POLEARM, STAFF).collect(Collectors.toSet()), RAL, TIR, TAL, SOL),
    LAST_WISH("Last Wish", () -> Stream.of(SWORD, HAMMER, AXE).collect(Collectors.toSet()), JAH, MAL, JAH, SUR, JAH, BER),
    LAWBRINGER("Lawbringer", () -> Stream.of(SWORD, HAMMER, SCEPTER).collect(Collectors.toSet()), AMN, LEM, KO),
    OATH("Oath", () -> Stream.of(SWORD, AXE, MACE).collect(Collectors.toSet()), SHAEL, PUL, MAL, LUM),
    OBEDIENCE("Obedience", () -> Stream.of(POLEARM).collect(Collectors.toSet()), HEL, KO, THUL, ETH, FAL),
    PHOENIX("Phoenix", () -> Stream.concat(ItemTypeContainer.WEAPON.getValues().stream(),
            ItemTypeContainer.SHIELD.getValues().stream()).collect(Collectors.toSet()), VEX, VEX, LO, JAH),
    PRIDE("Pride", () -> Stream.of(POLEARM).collect(Collectors.toSet()), CHAM, SUR, IO, LO),
    RIFT("Rift", () -> Stream.of(POLEARM, SCEPTER).collect(Collectors.toSet()), HEL, KO, LEM, GUL),
    SPIRIT("Spirit", () -> Stream.concat(ItemTypeContainer.SHIELD.getValues().stream(),
            Stream.of(SWORD)).collect(Collectors.toSet()), TAL, THUL, ORT, AMN),
    VOICE_OF_REASON("Voice of Reason", () -> Stream.of(SWORD, MACE).collect(Collectors.toSet()), LEM, KO, EL, ELD),
    WRATH("Wrath", ItemTypeContainer.MISSILE_WEAPON::getValues, PUL, LUM, BER, MAL),

    /* 1.11 Runewords */
    BONE("Bone", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), SOL, UM, UM),
    ENLIGHTENMENT("Enlightenment", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), PUL, RAL, SOL),
    MYTH("Myth", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), HEL, AMN, NEF),
    PEACE("Peace", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), SHAEL, THUL, AMN),
    PRINCIPLE("Principle", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), RAL, GUL, ELD),
    RAIN("Rain", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), ORT, MAL, ITH),
    TREACHERY("Treachery", () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), SHAEL, THUL, LEM);

    private final String name;
    private final String word;
    private final Map<Rune, Long> runes;
    private final Set<ItemType> types;

    Runeword(final String name, final Supplier<Set<ItemType>> types, final Rune... runes)
    {
        assert name != null;
        assert types != null;
        assert runes != null;
        assert runes.length > 0;

        this.name = name;
        this.word = Arrays.stream(runes).map(Rune::getName).collect(Collectors.joining());
        /* Sort the runes in descending order, map to Rune->Quantity, and make map read-only. */
        this.runes = Arrays.stream(runes)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()),
                        Collections::unmodifiableMap));
        this.types = Collections.unmodifiableSet(types.get());
    }

    /**
     * @return name of the Runeword
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return runeword sequence
     */
    public String getWord()
    {
        return word;
    }

    /**
     * @return number of sockets needed for the runeword
     */
    public int getRequiredSockets()
    {
        return runes.values().stream().mapToInt(v -> v.intValue()).sum();
    }

    /**
     * @return
     */
    public Map<Rune, Long> getRunes()
    {
        return runes;
    }

    public Set<ItemType> getTypes()
    {
        return types;
    }

    @Override public String toString()
    {
        return "\"" + name + "\" " + word;
    }

    public static class Comparator implements java.util.Comparator<Runeword>
    {
        @Override public int compare(final Runeword o1, final Runeword o2)
        {
            if (o1 == o2) return 0;

            final Iterator<Map.Entry<Rune, Long>> iter_o1 = o1.runes.entrySet().iterator(),
                    iter_o2 = o2.runes.entrySet().iterator();

            while (true)
            {
                if (!iter_o1.hasNext())
                    return iter_o2.hasNext() ? -1 : 0;
                if (!iter_o2.hasNext())
                    return 1;

                final Map.Entry<Rune, Long> o1_rune = iter_o1.next(), o2_rune = iter_o2.next();

                // The runeword with the higher diablo.rune trumps those with lower runes.
                final int rune_compare = o1_rune.getKey().compareTo(o2_rune.getKey());
                if (rune_compare != 0) return rune_compare;

                // If they both have the same diablo.rune, compare based on quantity of that diablo.rune.
                final int quantity_compare = Long.compare(o1_rune.getValue(), o2_rune.getValue());
                if (quantity_compare != 0) return quantity_compare;
            }
        }
    }
}
