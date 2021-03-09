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

package com.kevintyrrell.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines an pseudo-extension for enumerations.
 *
 * @since 3.0
 */
public class EnumExtendable<T extends Enum<?>> implements Queryable<T>
{
    private final CachedValue<List<T>> values;
    private final CachedValue<Map<String, T>> stringMap;

    /**
     * Constructs an extension of the Enum, having been provided the class.
     *
     * @param cls Class of the enum.
     */
    public EnumExtendable(final Class<T> cls)
    {
        values = new CachedValue<>()
        {
            @Override protected List<T> recalculate()
            {
                return Arrays.stream(requireNonNull(cls).getEnumConstants())
                        .collect(Collectors.collectingAndThen(
                                Collectors.toList(), Collections::unmodifiableList));
            }
        };

        stringMap = new CachedValue<>()
        {
            @Override protected Map<String, T> recalculate()
            {
                return Queryable.createStringMap(values.get().stream(), EnumExtendable.this::stringMapKeyer);
            }
        };
    }

    /**
     * Keys the specified enum value into the string map.
     *
     * By default, the keys are instantiated by the following:
     *      `value.toString.toLowerCase()`
     *
     * Override this method to allow for control over keying.
     *
     * @param value Value to be parsed.
     * @return Key used for enum value lookups by string.
     */
    protected String stringMapKeyer(final T value)
    {
        assert value != null;
        return value.toString().toLowerCase();
    }

    /**
     * Read-only list of all values in the enum.
     *
     * This method should be used as an alternative to Enum#values().
     * Enum#values() generates a new array each call, and should be avoided.
     *
     * @return Read-only list of all values in the enum.
     */
    public List<T> values()
    {
        return values.get();
    }

    /**
     * Queries the number of values in the enumeration.
     *
     * With normal enums this operation can only be performed by calling:
     *      *  `values().length`, which has a linear overhead of complexity and storage.
     *      *  or manually referencing the last enum element and calling ordinal(),
     *      *  which is bug prone when enum values are shifted in order/removed/added.
     *
     * @return Number of values in the enumeration.
     */
    public int size()
    {
        return values.get().size();
    }

    /**
     * Retrieves an enum value based its respective ordinal value.
     *
     * Enum ordinal values start at 0, increase sequentially, and
     * are assigned in the order of which the enum values are defined.
     *
     * @see Enum#ordinal()
     * @param ordinal Ordinal to query.
     * @return Enum element associated with the ordinal value.
     */
    public T fromOrdinal(final int ordinal)
    {
        final List<T> values = this.values.get();
        if (ordinal < 0 || ordinal >= values.size())
            throw new IndexOutOfBoundsException(ordinal);
        return values.get(ordinal);
    }

    /**
     * Parses the formal name of an enum value.
     *
     * e.g. 'APPLE' -> 'Apple'
     *
     * @see Enum#name()
     * @param value Enum value to parse.
     * @return formal name of the enum value, based on name().
     */
    public static String formalName(final Enum<?> value)
    {
        final String name = requireNonNull(value.name()).toLowerCase();
        final char first = name.charAt(0);
        assert first >= 'a' && first <= 'z'; // This should be guaranteed thanks to the compiler.
        return Character.toUpperCase(first) + name.substring(1);
    }

    /**
     * Read-only map which associates string representations
     * of objects to their respective constant object values.
     * <p>
     * By default, map keys are set to Object#toString(). This behavior
     * can be changed by overloading StringToValue#createStringMap().
     *
     * @return Read-only map associating string representations to object values.
     * @see Queryable#createStringMap(Stream, Function)
     */
    @Override public Map<String, T> stringMap()
    {
        return stringMap.get();
    }
}
