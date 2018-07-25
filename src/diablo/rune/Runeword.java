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
import java.util.concurrent.atomic.AtomicInteger;
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
    ANCIENTS_PLEDGE("Ancient's Pledge", 21, ItemTypeContainer.SHIELD::getValues, RAL, ORT, TAL),
    BLACK("Black", 35, () -> Stream.of(CLUB, HAMMER, MACE).collect(Collectors.toSet()), THUL, IO, NEF),
    FURY("Fury", 65, ItemTypeContainer.MELEE_WEAPON::getValues, JAH, GUL, ETH),
    HOLY_THUNDER("Holy Thunder", 23, () -> Stream.of(SCEPTER).collect(Collectors.toSet()), ETH, RAL, ORT, TAL),
    HONOR("Honor", 27, ItemTypeContainer.MELEE_WEAPON::getValues, AMN, EL, ITH, TIR, SOL),
    KINGS_GRACE("King's Grace", 25, () -> Stream.of(SWORD, SCEPTER).collect(Collectors.toSet()), AMN, RAL, THUL),
    LEAF("Leaf", 19, () -> Stream.of(STAFF).collect(Collectors.toSet()), TIR, RAL),
    LIONHEART("Lionheart", 41, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), HEL, LUM, FAL),
    LORE("Lore", 27, () -> Stream.of(HELM).collect(Collectors.toSet()), ORT, SOL),
    MALICE("Malice", 15, ItemTypeContainer.MELEE_WEAPON::getValues, ITH, EL, ETH),
    MELODY("Melody", 39, ItemTypeContainer.MISSILE_WEAPON::getValues, SHAEL, KO, NEF),
    MEMORY("Memory", 37, () -> Stream.of(STAFF).collect(Collectors.toSet()), LUM, IO, SOL, ETH),
    NADIR("Nadir", 13, () -> Stream.of(HELM).collect(Collectors.toSet()), NEF, TIR),
    RADIANCE("Radiance", 27, () -> Stream.of(HELM).collect(Collectors.toSet()), NEF, SOL, ITH),
    RHYME("Rhyme", 29, ItemTypeContainer.SHIELD::getValues, SHAEL, ETH),
    SILENCE("Silence", 55, ItemTypeContainer.WEAPON::getValues, DOL, ELD, HEL, IST, TIR, VEX),
    SMOKE("Smoke", 37, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), NEF, LUM),
    STEALTH("Stealth", 17, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), TAL, ETH),
    STEEL("Steel", 13, () -> Stream.of(SWORD, AXE, MACE).collect(Collectors.toSet()), TIR, EL),
    STRENGTH("Strength", 25, ItemTypeContainer.MELEE_WEAPON::getValues, AMN, TIR),
    VENOM("Venom", 49, () -> Stream.concat(ItemTypeContainer.WEAPON.getValues().stream(),
            Stream.of(ORB)).collect(Collectors.toSet()), TAL, DOL, MAL),
    WEALTH("Wealth", 43, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), LEM, KO, TIR),
    WHITE("White", 35, () -> Stream.of(WAND).collect(Collectors.toSet()), DOL, IO),
    ZEPHYR("Zephyr", 21, ItemTypeContainer.MISSILE_WEAPON::getValues, ORT, ETH),

    /* 1.10 Runewords */
    BEAST("Beast", 63, () -> Stream.of(AXE, SCEPTER, HAMMER).collect(Collectors.toSet()), BER, TIR, UM, MAL, LUM),
    BRAMBLE("Bramble", 61, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), RAL, OHM, SUR, ETH),
    BREATH_OF_THE_DYING("Breath of the Dying", 69, ItemTypeContainer.WEAPON::getValues, VEX, HEL, EL, ELD, ZOD, ETH),
    CALL_TO_ARMS("Call to Arms", 57, ItemTypeContainer.WEAPON::getValues, AMN, RAL, MAL, IST, OHM),
    CHAINS_OF_HONOR("Chains of Honor", 63, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), DOL, UM, BER, IST),
    CHAOS("Chaos", 57, () -> Stream.of(CLAW).collect(Collectors.toSet()), FAL, OHM, UM),
    CRESCENT_MOON("Crescent Moon", 47, () -> Stream.of(AXE, SWORD, POLEARM).collect(Collectors.toSet()), SHAEL, UM, TIR),
    DELIRIUM("Delirium", 51, () -> Stream.of(HELM).collect(Collectors.toSet()), LEM, IST, KO),
    DOOM("Doom", 67, () -> Stream.of(AXE, POLEARM, HAMMER).collect(Collectors.toSet()), HEL, OHM, UM, LO, CHAM),
    DURESS("Duress", 47, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), SHAEL, UM, THUL),
    ENIGMA("Engima", 65, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), JAH, ITH, BER),
    ETERNITY("Eternity", 63, ItemTypeContainer.MELEE_WEAPON::getValues, AMN, BER, IST, SOL, SUR),
    EXILE("Exile", 57, () -> Stream.of(AURIC_SHIELD).collect(Collectors.toSet()), VEX, OHM, IST, DOL),
    FAMINE("Famine", 65, () -> Stream.of(AXE, HAMMER).collect(Collectors.toSet()), FAL, OHM, ORT, JAH),
    GLOOM("Gloom", 47, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), FAL, UM, PUL),
    HAND_OF_JUSTICE("Hand of Justice", 67, ItemTypeContainer.WEAPON::getValues, SUR, CHAM, AMN, LO),
    HEART_OF_THE_OAK("Heart of the Oak", 55, () -> Stream.of(STAFF, MACE).collect(Collectors.toSet()), KO, VEX, PUL, THUL),
    KINGSLAYER("Kingslayer", 53, () -> Stream.of(SWORD, AXE).collect(Collectors.toSet()), MAL, UM, GUL, FAL),
    PASSION("Passion", 43, ItemTypeContainer.WEAPON::getValues, DOL, ORT, ELD, LEM),
    PRUDENCE("Prudence", 49, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), MAL, TIR),
    SANCTUARY("Sanctuary", 49, ItemTypeContainer.SHIELD::getValues, KO, KO, MAL),
    SPLENDOR("Splendor", 37, ItemTypeContainer.SHIELD::getValues, ETH, LUM),
    STONE("Stone", 47, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), SHAEL, UM, PUL, LUM),
    WIND("Wind", 61, ItemTypeContainer.MELEE_WEAPON::getValues, SUR, EL),

    /* 1.10 Runewords (Ladder Only) */
    BRAND("Brand", 65, ItemTypeContainer.MISSILE_WEAPON::getValues, JAH, LO, MAL, GUL),
    DEATH("Death", 55, () -> Stream.of(SWORD, AXE).collect(Collectors.toSet()), HEL, EL, VEX, ORT, GUL),
    DESTRUCTION("Destruction", 65, () -> Stream.of(POLEARM, SWORD).collect(Collectors.toSet()), VEX, LO, BER, JAH, KO),
    DRAGON("Dragon", 61, () -> Stream.concat(ItemTypeContainer.SHIELD.getValues().stream(),
            Stream.of(BODY_ARMOR)).collect(Collectors.toSet()), SUR, LO, SOL),
    DREAM("Dream", 65, () -> Stream.concat(ItemTypeContainer.SHIELD.getValues().stream(),
            Stream.of(HELM)).collect(Collectors.toSet()), IO, JAH, PUL),
    EDGE("Edge", 25, ItemTypeContainer.MISSILE_WEAPON::getValues, TIR, TAL, AMN),
    FAITH("Faith", 65, ItemTypeContainer.MISSILE_WEAPON::getValues, OHM, JAH, LEM, ELD),
    FORTITUDE("Fortitude", 59, () -> Stream.concat(ItemTypeContainer.WEAPON.getValues().stream(),
            Stream.of(BODY_ARMOR)).collect(Collectors.toSet()), EL, SOL, DOL, LO),
    GRIEF("Grief", 59, () -> Stream.of(SWORD, AXE).collect(Collectors.toSet()), ETH, TIR, LO, MAL, RAL),
    HARMONY("Harmony", 39, ItemTypeContainer.MISSILE_WEAPON::getValues, TIR, ITH, SOL, KO),
    ICE("Ice", 65, ItemTypeContainer.MISSILE_WEAPON::getValues, AMN, SHAEL, JAH, LO),
    INFINITY("Infinity", 63, () -> Stream.of(POLEARM).collect(Collectors.toSet()), BER, MAL, BER, IST),
    INSIGHT("Insight", 27, () -> Stream.of(POLEARM, STAFF).collect(Collectors.toSet()), RAL, TIR, TAL, SOL),
    LAST_WISH("Last Wish", 65, () -> Stream.of(SWORD, HAMMER, AXE).collect(Collectors.toSet()), JAH, MAL, JAH, SUR, JAH, BER),
    LAWBRINGER("Lawbringer", 43, () -> Stream.of(SWORD, HAMMER, SCEPTER).collect(Collectors.toSet()), AMN, LEM, KO),
    OATH("Oath", 59, () -> Stream.of(SWORD, AXE, MACE).collect(Collectors.toSet()), SHAEL, PUL, MAL, LUM),
    OBEDIENCE("Obedience", 41, () -> Stream.of(POLEARM).collect(Collectors.toSet()), HEL, KO, THUL, ETH, FAL),
    PHOENIX("Phoenix", 65, () -> Stream.concat(ItemTypeContainer.WEAPON.getValues().stream(),
            ItemTypeContainer.SHIELD.getValues().stream()).collect(Collectors.toSet()), VEX, VEX, LO, JAH),
    PRIDE("Pride", 67, () -> Stream.of(POLEARM).collect(Collectors.toSet()), CHAM, SUR, IO, LO),
    RIFT("Rift", 53, () -> Stream.of(POLEARM, SCEPTER).collect(Collectors.toSet()), HEL, KO, LEM, GUL),
    SPIRIT("Spirit", 25, () -> Stream.concat(ItemTypeContainer.SHIELD.getValues().stream(),
            Stream.of(SWORD)).collect(Collectors.toSet()), TAL, THUL, ORT, AMN),
    VOICE_OF_REASON("Voice of Reason", 43, () -> Stream.of(SWORD, MACE).collect(Collectors.toSet()), LEM, KO, EL, ELD),
    WRATH("Wrath", 63, ItemTypeContainer.MISSILE_WEAPON::getValues, PUL, LUM, BER, MAL),

    /* 1.11 Runewords */
    BONE("Bone", 47, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), SOL, UM, UM),
    ENLIGHTENMENT("Enlightenment", 45, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), PUL, RAL, SOL),
    MYTH("Myth", 25, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), HEL, AMN, NEF),
    PEACE("Peace", 29, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), SHAEL, THUL, AMN),
    PRINCIPLE("Principle", 55, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), RAL, GUL, ELD),
    RAIN("Rain", 49, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), ORT, MAL, ITH),
    TREACHERY("Treachery", 43, () -> Stream.of(BODY_ARMOR).collect(Collectors.toSet()), SHAEL, THUL, LEM);

    /* Name of the Runeword. */
    private final String name;
    /* Word which activates the Runeword. */
    private final String word;
    /* Runes which the Runeword requires, in order. */
    private final Map<Rune, Integer> runes;
    /* Item types in which this Runeword can go into. */
    private final Set<ItemType> types;
    /* Required level for the Runeword. */
    private final int level;
    /* Summed rarity of all of the Runes. */
    private final double rarity;

    /* Threshold of when a Runeword should be tracked. */
    public static final float COMPLETION_THRESHOLD = 0.25f;
    /* Read-only map sorted by most rare Runeword -> most common. */
    public static final Map<Runeword, Integer> RANKINGS;
    
    static
    {
        /* Rank every Runeword. The most rare Runeword is #1, followed by #2, etc. */
        final AtomicInteger rankCounter = new AtomicInteger();        
        RANKINGS = Arrays.stream(Runeword.values())
                .sorted(new Runeword.Comparator().reversed())
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Function.identity(), value -> rankCounter.incrementAndGet(), 
                                (a, b) -> a, LinkedHashMap::new), 
                        Collections::unmodifiableMap));
    }

    Runeword(final String name, final int level, final Supplier<Set<ItemType>> types, final Rune... runes)
    {
        assert name != null;
        assert level >= 1;
        assert types != null;
        assert runes != null;
        assert runes.length > 0;

        this.name = name;
        this.level = level;
        /* Sort the Runes in descending order, map to Rune->Quantity, and make map read-only. */
        this.runes = Arrays.stream(runes)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.summingInt(e -> 1)),
                        Collections::unmodifiableMap));
        /* Only include Item types which can actually have enough sockets to support the Runeword. */
        this.types = types.get().stream()
                .filter(t -> t.getMaxSockets() >= runes.length)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> EnumSet.noneOf(ItemType.class)),
                        Collections::unmodifiableSet));
        word = Arrays.stream(runes).map(Rune::getName).collect(Collectors.joining());
        /* Sum the total rarity of all the Runes in the word. */
        rarity = this.runes.entrySet().stream()
                .mapToDouble(entry -> entry.getValue() * entry.getKey().getRarity())
                .sum();
    }

    /**
     * Calculates a progress percentage for the Runeword to be completed.
     * A Runeword is 100% complete when all Runes it requires are collected.
     * Runes have weighted rarities, so higher Runes make more of an impact.
     * @param runes Runes the user owns.
     * @return percentage of completion (between 0.0% and 1.0% inclusive).
     */
    public double calculateProgress(final Map<Rune, Integer> runes)
    {
        assert runes != null;
        return this.runes.entrySet().stream()
                .filter(entry -> runes.containsKey(entry.getKey()))
                .mapToDouble(entry -> 
                {
                    final Rune r = entry.getKey();
                    return Math.min(entry.getValue(), runes.get(r)) * calculateProgress(r);
                })
                .sum();
    }

    /**
     * Calculates a progress percentage for the Runeword to be completed.
     * A Runeword is 100% complete when all Runes it requires are collected.
     * Runes have weighted rarities, so higher Runes make more of an impact.
     * @param rune Rune the user owns.
     * @return percentage of completion (between 0.0% and 1.0% inclusive).
     */
    public double calculateProgress(final Rune rune)
    {
        assert rune != null;
        return rune.getRarity() / rarity;
    }

    /**
     * @return Name of the Runeword.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return Sequence of Runes of the Runeword
     */
    public String getWord()
    {
        return word;
    }

    /**
     * @return Number of sockets required for the Runeword.
     */
    public int getRequiredSockets()
    {
        return runes.values().stream()
                .mapToInt(e -> e)
                .sum();
    }

    /**
     * @return Runes and their quantity for the Runeword.
     */
    public Map<Rune, Integer> getRunes()
    {
        return runes;
    }

    /**
     * @return Types the Runeword is applicable for.
     */
    public Set<ItemType> getTypes()
    {
        return types;
    }

    /**
     * @return Required level to use the Runeword.
     */
    public int getLevel()
    {
        return level;
    }

    private static final Map<String, Runeword> fromString = Arrays.stream(Runeword.values())
            .collect(Collectors.collectingAndThen(
                    Collectors.toMap(k -> k.name().toLowerCase(), Function.identity()),
                    Collections::unmodifiableMap));

    /**
     * Looks up a Runeword from a String.
     * @param str String to use for the lookup.
     * @return Runeword corresponding to the String.
     */
    public static Runeword fromString(final String str)
    {
        assert str != null;
        return fromString.get(str);
    }

    @Override public String toString()
    {
        return "\"" + name + "\" " + word;
    }

    public static class Comparator implements java.util.Comparator<Runeword>
    {
        /**
         * Sums up the rarity of each Rune of a specified Runeword.
         * @param rw Runeword to calculate.
         * @return Rarity of the Runeword.
         */
        private static double calculateRarity(final Runeword rw)
        {
            assert rw != null;
            return rw.getRunes().entrySet().stream()
                    .mapToDouble(runeEntry -> runeEntry.getKey().getRarity() * runeEntry.getValue())
                    .sum();
        }

        @Override public int compare(final Runeword o1, final Runeword o2)
        {
            assert o1 != null;
            assert o2 != null;
            if (o1 == o2) return 0;
            return Double.compare(calculateRarity(o1), calculateRarity(o2));
        }
    }
}
