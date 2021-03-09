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

package com.kevintyrrell.diablo.runeword;

import com.kevintyrrell.diablo.item.ItemType;
import com.kevintyrrell.diablo.rune.Rune;
import com.kevintyrrell.util.JSONLoader;
import com.kevintyrrell.util.Queryable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.toIntExact;

/**
 * Defines an simple class where Runewords are loaded from the storage medium.
 *
 * @since 3.0
 */
public class RunewordLoader implements Queryable<Runeword>
{
    private final Map<String, Runeword> stringMap;

    /**
     * Instantiates a runeword loader instance.
     *
     * Upon creation, loads Runewords from the storage medium.
     */
    public RunewordLoader()
    {
        final JSONArray jo = (JSONArray)JSONLoader.parseJSON("Runewords");
        assert jo != null;
        stringMap = Queryable.createStringMap(
                /* IntStream is require here to avoid generic casting in a Stream#map call. */
                IntStream.range(0, jo.size())
                        .mapToObj(i -> loadRuneword((JSONObject)jo.get(i))), rw ->
                {
                    final String rwlc = rw.getName().toLowerCase();
                    /* Remove symbols and spaces. Replace spaces with underscores. */
                    return rwlc.replace("'", "").replace(" ", "_");
                });
    }

    /* Loads a Runeword from its JSON string. */
    @SuppressWarnings("unchecked")
    private static Runeword loadRuneword(final JSONObject jsonRW)
    {
        assert jsonRW != null;
        final String name = (String)jsonRW.get("name");
        final int level = toIntExact(((Long)jsonRW.get("level")));
        final JSONArray jsonItemTypeArray = (JSONArray)jsonRW.get("bases");
        final Stream<ItemType> types = ((Stream<Object>)jsonItemTypeArray.stream())
                .map(obj -> ItemType.extension.fromString((String)obj));
        final JSONArray jsonRuneArray = (JSONArray)jsonRW.get("runes");
        final Stream<Rune> runes = ((Stream<Object>)jsonRuneArray.stream())
                .map(obj -> Rune.extension.fromOrdinal(toIntExact((Long)obj)));
        final String description = (String)jsonRW.get("description");
        return new Runeword(name, level, description, types, runes);
    }

    /**
     * Read-only map which associates string representations
     * of objects to their respective constant object values.
     * <p>
     * By default, map keys are set to Object#toString(). This behavior
     * can be changed by overloading StringToValue#createStringMap().
     * <p>
     * This method should be implemented such that the returned map is
     * an instance variable of the class which is implementing this method.
     *
     * @return Read-only map associating string representations to object values.
     */
    @Override public Map<String, Runeword> stringMap()
    {
        return stringMap;
    }
}
