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

package com.kevintyrrell.lang;

import com.kevintyrrell.util.CachedValue;
import com.kevintyrrell.util.JSONLoader;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
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
    /* JSON object for all of the locale data. */
    private static final JSONObject jo = (JSONObject)JSONLoader.parseJSON("Localization");
    /* Set to trigger invalidation(s) when the locale is changed. */
    private static final WeakHashMap<CachedValue<String>, Object> invalidators = new WeakHashMap<>();

    /* The currently selected locale. */
    private static Locale active = DEFAULT_LOCALE;

    /* Cached strings for the locale. */
    private final Map<String, String> cachedPaths = new HashMap<>();

    /**
     * Retrieves a JSON String value which is notified of locale changes.
     *
     * Locale#get() should only be called ideally once per runtime, per string use case.
     * Saving a reference to the returned cached value is highly recommended.
     * The cached value instance will be notified of changes to the locale.
     * Any change in the locale will trigger a re-lookup on the next CachedValue#get() call.
     *
     * @param path Path through the fields of the JSON file.
     * @return Cached value of the String value at the path.
     */
    public static CachedValue<String> get(final String path)
    {
        final CachedValue<String> cv = new CachedValue<>()
        {
            @Override protected String recalculate()
            {
                String value = active.cachedPaths.get(requireNonNull(path));
                if (value == null)
                {
                    value = JSONLoader.find(jo, active.toString() + '/' + path);
                    active.cachedPaths.put(path, value);
                }

                return value;
            }
        };

        /* Value is a dummy object in this case, using map as a set. */
        invalidators.put(cv, jo);
        return cv;
    }

    /**
     * @return Get the current locale.
     */
    public static Locale getLocale()
    {
        return active;
    }

    /**
     * Sets the locale.
     *
     * Upon a locale change, all cached values are invalidated and
     * will re-acquire their localized strings on their next call.
     *
     * @param locale Locale to be set.
     */
    public static void setLocale(final Locale locale)
    {
        if (requireNonNull(locale) == active) return;
        active = locale;
        invalidators.keySet().forEach(CachedValue::invalidate);
    }

    /**
     * @return String representation of the locale.
     */
    @Override public String toString()
    {
        return name().toLowerCase();
    }
}
