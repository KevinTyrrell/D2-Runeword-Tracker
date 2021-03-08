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

package com.KevinTyrrell.lang;

import com.KevinTyrrell.util.CachedValue;
import com.KevinTyrrell.util.JSONLoader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.WeakHashMap;

import static java.util.Objects.requireNonNull;

/**
 * Defines a layout of all strings used in the program.
 *
 * @since 3.0
 */
public enum Locale
{
    EN;

    /* Default to English. */
    private static final Locale DEFAULT_LOCALE = EN;
    /* Path to the JSON file. */
    private static final String JSON_PATH = "src/main/resources/json/Locale.json";
    /* JSON object for all of the locale data. */
    private static final JSONObject jo = JSONLoader.parseJSON("Localization");

    /* The currently selected locale. */
    private static Locale active = DEFAULT_LOCALE;

    /* Set to trigger invalidation(s) when the locale is changed. */
    private final WeakHashMap<CachedValue<String>, Boolean> invalidators = new WeakHashMap<>();

    /* Cached strings for the locale. */
    private final Map<String, String> shortPaths = new HashMap<>();


    /**
     * @return Get the current locale.
     */
    public static Locale getLocale()
    {
        return active;
    }
}
