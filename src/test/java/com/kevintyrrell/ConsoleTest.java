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
import com.kevintyrrell.model.util.CachedValue;
import com.kevintyrrell.view.ConsoleColor;
import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Test public void consoleTest4()
    {
        // Testing colors.
        System.out.println(ConsoleColor.YELLOW_UNDERLINED.wrap("Hello, World!"));
        assertTrue(true);
    }

    @Test public void consoleTest5()
    {
        /* Current problem: Stylized strings report wrong length invalid length. */
        final String str1 = "Hello, World!"; // Length: 13
        final String str2 = ConsoleColor.CYAN_BACKGROUND.wrap(str1); // Length: 22
        assertNotEquals(str1.length(), str2.length());
        System.out.println(str1);
        System.out.println(str2);
        /* This will break future Paragraph-related code -- need a way to detect it. */

        final String str3 = "Hello; [0World! How are you?\\033Today?";
        final char[] str3Chars = {
                'H', 'e', 'l', 'l', 'o', ';', ' ', '[', '0', 'W', 'o', 'r', 'l', 'd',
                '!', ' ', 'H', 'o', 'w', ' ', 'a', 'r', 'e', ' ', 'y', 'o', 'u',
                '?', '\\', '0', '3', '3', 'T', 'o', 'd', 'a', 'y', '?' };
        assertArrayEquals(str3.toCharArray(), str3Chars);

        final String str4 = ConsoleColor.YELLOW_BACKGROUND_BRIGHT.wrap(str3);
        System.out.println(Arrays.toString(str4.toCharArray()));

        final String regex = ConsoleColor.ESCAPE_COLOR_FORMAT;
        final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(str4);

        assertTrue(m.find());
        assertEquals(str3, m.group(1));
    }
}
