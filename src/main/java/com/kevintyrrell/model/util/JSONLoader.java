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

package com.kevintyrrell.model.util;

import com.kevintyrrell.lang.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.MissingResourceException;

import static java.util.Objects.requireNonNull;

/**
 * Defines a utility for loading JSON objects.
 *
 * @since 3.0
 */
public interface JSONLoader
{
    /**
     * Relatively path to the JSON directory.
     */
    String JSON_RELATIVE_PATH = "src/main/resources/json/";

    /**
     * Parses a specific JSON resource from the storage medium.
     *
     * @param filename Filename of the JSON file, excluding extension.
     * @return JSON object or array which was loaded.
     */
    static JSONAware parseJSON(String filename)
    {
        filename = JSON_RELATIVE_PATH + requireNonNull(filename) + ".json";
        final JSONParser parser = new JSONParser();
        try (final FileReader reader = new FileReader(filename))
        {
            return (JSONAware)parser.parse(reader);
        }
        catch (final FileNotFoundException e)
        {
            e.printStackTrace();
            throw new MissingResourceException(filename, Locale.class.getSimpleName(), "JSON file missing");
        }
        catch (final IOException | ParseException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Attempts to traverse the JSON object or array, following a path.
     *
     * @param root JSON object or array to begin traversal.
     * @param path Path through the JSON structure to the desired value.
     * @return Value at the end of the path.
     */
    static String find(final JSONAware root, final String path)
    {
        Object current = requireNonNull(root);
        for (final String key : requireNonNull(path).split("/"))
        {
            if (current instanceof JSONObject)
                current = ((JSONObject) current).get(key);
            else if (current instanceof JSONArray)
            {
                try
                {
                    final JSONArray jsa = (JSONArray)current;
                    final int index = Integer.parseInt(key);
                    if (index < 0 || index >= jsa.size())
                        throw new IllegalArgumentException("JSON path index is out of bounds: ".concat(key));
                    current = jsa.get(index);
                }
                catch (final NumberFormatException e)
                {
                    throw new IllegalArgumentException("JSON path index is invalid: ".concat(key));
                }
            }
            else throw new UnsupportedOperationException();
        }

        if (!(current instanceof String))
            throw new IllegalArgumentException("Inner JSON path was invalid: ".concat(path));
        return (String)current;
    }
}
