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
 * File Name:       Rune
 * File Author:     Kevin Tyrrell
 * Date Created:    03/09/2018
 */

import util.Utilities;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum Rune
{
    /* Drop Source: https://diablo2.diablowiki.net/Guide:Rune_Finder_Guide_v1.10,_by_Urlik */
    EL("El", 215493),
    ELD("Eld", 143662),
    TIR("Tir", 119718),
    NEF("Nef", 79812),
    ETH("Eth", 83803),
    ITH("Ith", 55868),
    TAL("Tal", 69836),
    RAL("Ral", 46557),
    ORT("Ort", 48885),
    THUL("Thul", 32590),
    AMN("Amn", 29874),
    SOL("Sol", 19916),
    SHAEL("Shael", 15767),
    DOL("Dol", 10511),
    HEL("Hel", 8102),
    IO("Io", 5402),
    LUM("Lum", 4107),
    KO("Ko", 2738),
    FAL("Fal", 2068),
    LEM("Lem", 1379),
    PUL("Pul", 1038),
    UM("Um", 692),
    MAL("Mal", 594),
    IST("Ist", 396),
    GUL("Gul", 340),
    VEX("Vex", 226),
    OHM("Ohm", 194),
    LO("Lo", 129),
    SUR("Sur", 111),
    BER("Ber", 74),
    JAH("Jah", 63),
    CHAM("Cham", 42),
    ZOD("Zod", 12);

    /* Name of the Rune. */
    private final String name;
    /* Rarity of the Rune from (0, ∞). The higher the value, the more rare the Rune is. */
    private final double rarity;
    
    /* Threshold of what is considered to be a High Rune [Mal, Zod]. */
    public static final Rune HIGH_RUNE_THRESH = MAL;
    /* Each Rune's drop value is out of 1,000,000M total Rune drops. */
    private static final int DROP_SAMPLE_SIZE = 1000000;
    
    /**
     * @param name - Name of the Rune.
     * @param dropValue - Number of times dropped per one million Rune drops.
     */
    Rune(final String name, final int dropValue)
    {
        assert name != null;
        assert dropValue > 0;
        this.name = name;
        rarity = 1 / ((double)dropValue / DROP_SAMPLE_SIZE);
    }

    /**
     * @return True if this Rune is considered to be a High Rune.
     */
    public boolean isHighRune()
    {
        return compareTo(HIGH_RUNE_THRESH) >= 0;
    }

    /**
     * @return Name of the Rune.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return Percent chance for the Rune to drop (from [0, 1]).
     */
    public double getRarity()
    {
        return rarity;
    }

    /**
     * Parses a stream of Strings into Runes and their respective quantities.
     * Strings must either be of the form "{RuneName}{RuneQuantity}" or
     * "{RuneName} (the later assumes a quantity of one). If the Stream consists
     * of Amn5, amn4, the map will sum the two into the pair: amn->9.
     * @param ss String Stream to parse.
     * @return Map of all of the Runes corresponding to their summed quantities.
     */
    public static Map<Rune, Integer> parseRuneQuantities(final Stream<String> ss)
    {
        assert ss != null;

        /* Regular expression of non-digit followed by a digit. */
        final String regexp = "(?<=\\D)(?=\\d)";
        final int MAX_RUNE_QUANTITY = 99;

        return ss.map(str -> str.split(regexp))
                .flatMap(sep ->
                {
                    final Rune r = Rune.fromString(sep[0]);
                    if (r == null)
                    {
                        System.out.printf("Error: No such Rune of \"%.10s\" exists.\n\n", sep[0]);
                        return Stream.empty();
                    }

                    final int q;
                    if (sep.length > 1)
                        try
                        {
                            q = Integer.parseInt(sep[1]);
                            if (q < 1 || q > MAX_RUNE_QUANTITY)
                                throw new NumberFormatException();
                        }
                        catch (final NumberFormatException e)
                        {
                            System.out.printf("Error: Rune quantity of \"%.10s\" must be between [1, %d].\n\n",
                                    sep[1], MAX_RUNE_QUANTITY);
                            return Stream.empty();
                        }
                    /* If no Rune quantity is provided, assume 1. */
                    else q = 1;

                    return Stream.of(Map.entry(r, q));
                })
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                Integer::sum, () -> new EnumMap<>(Rune.class)),
                        Collections::unmodifiableMap));
    }

    private static final Map<String, Rune> fromString = Arrays.stream(Rune.values())
            .collect(Collectors.collectingAndThen(
                    Collectors.toMap(k -> k.getName().toLowerCase(), Function.identity()),
                    Collections::unmodifiableMap));

    /**
     * Looks up a Rune from a String.
     * @param str String to use for the lookup.
     * @return Rune corresponding to the String.
     */
    public static Rune fromString(final String str)
    {
        assert str != null;
        return fromString.get(str);
    }

    /**
     * @return String representation of the Rune.
     */
    @Override public String toString()
    {
        return name;
    }
}
