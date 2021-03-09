/*
 *     Application which tracks Runeword progress in the video game Diablo 2.
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

package com.kevintyrrell;

import com.kevintyrrell.lang.Locale;
import com.kevintyrrell.util.CachedValue;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit testing class.
 *
 * @since 3.0
 */
public class ConsoleTest
{
    @Test public void consoleTest1()
    {
        CachedValue<String> cv1 = Locale.get("runes/title");
        assertNotNull(cv1);
        assertEquals("RUNE COLLECTION", cv1.get());

        cv1 = Locale.get("runes/tiers/2");
        assertNotNull(cv1);
        assertEquals("Low", cv1.get());
    }

    @Test(expected = IllegalArgumentException.class) public void consoleTest2()
    {
        final CachedValue<String> cv2 = Locale.get("runes/tiers/3");
        assertNotNull(cv2);
        cv2.get();
    }

    @Test(expected = IllegalArgumentException.class) public void consoleTest3()
    {
        final CachedValue<String> cv3 = Locale.get("runes/tiers/a");
        assertNotNull(cv3);
        cv3.get();
    }
}
