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

    private final String name;
    private final double rarity;

    /**
     * @param name - Name of the Rune.
     * @param dropValue - Number of times dropped per one million Rune drops.
     */
    Rune(final String name, final long dropValue)
    {
        assert name != null;
        assert dropValue > 0;
        this.name = name;
        /* Rune's overall drop chance based on sample size of 1M. */
        this.rarity = dropValue / 1000000.0;
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

    @Override public String toString()
    {
        return name;
    }
}
