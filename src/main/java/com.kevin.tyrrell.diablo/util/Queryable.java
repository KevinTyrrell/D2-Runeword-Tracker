/*
 * Application which tracks Runeword progress in the video game Diablo 2.
 * Copyright (C) 2021  Kevin Tyrrell
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

package com.kevin.tyrrell.diablo.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Defines an interface to facilitate Strings -> Values.
 *
 * @param <T> Type of values which can be queried..
 * @since 3.0
 */
public interface Queryable<T>
{
    /**
     * Read-only map which associates string representations
     * of objects to their respective constant object values.
     *
     * By default, map keys are set to Object#toString(). This behavior
     * can be changed by overloading StringToValue#createStringMap().
     *
     * This method should be implemented such that the returned map is
     * an instance variable of the class which is implementing this method.
     *
     * @return Read-only map associating string representations to object values.
     * @see Queryable#createStringMap(Stream, Function)
     */
    Map<String, T> stringMap();

    /**
     * Retrieves an object value based on its respective string representation.
     *
     * By default, map keys are set to Object#toString(). This behavior
     * can be changed by overloading StringToValue#createStringMap().
     *
     * @param str String representation of the desired object value.
     * @return Object value corresponding to the string, or null.
     * @see #stringMap()
     * @see Queryable#createStringMap(Stream, Function)
     */
    default T fromString(final String str)
    {
        final Map<String, T> reverseMap = requireNonNull(stringMap());
        return reverseMap.get(requireNonNull(str));
    }

    /**
     * Creates an immutable map associating Strings -> Values.
     *
     * The parameter list of values should be complete and immutable.
     * The resulting map should then be saved as an instance variable,
     * and provided as the return value for calls to #stringMap().
     *
     * Keys are dictated by the value(s) returned by the specified callback.
     *
     * @param values Values to be associated in the map.
     * @param toStringCb Callback used to parse Objects -> Strings.
     * @param <T> Data type of the corresponding objects.
     * @return Map associating string representations with their respective object values.
     * @see #stringMap()
     */
    static <T> Map<String, T> createStringMap(final Stream<T> values,
                                                              final Function<T, String> toStringCb)
    {
        return toReverseMap(requireNonNull(values), requireNonNull(toStringCb));
    }

    /**
     * Creates an immutable map associating Strings -> Values.
     *
     * The parameter list of values should be complete and immutable.
     * The resulting map should then be saved as an instance variable,
     * and provided as the return value for calls to #stringMap().
     *
     * By default, map keys are set to enum.toString(). This behavior
     * can be changed by calling the overloaded function `createStringMap`.
     *
     * @param values Values to be associated in the map.
     * @param <T> Data type of the corresponding objects.
     * @return Map associating string representations with their respective object values.
     * @see #stringMap()
     * @see Queryable#createStringMap(Stream, Function)
     */
    static <T> Map<String, T> createStringMap(final Stream<T> values)
    {
        return toReverseMap(requireNonNull(values), Object::toString);
    }

    /* Helper function for #createStringMap(). */
    private static <T> Map<String, T> toReverseMap(final Stream<T> values, final Function<T, String> toStringCb)
    {
        assert values != null;
        assert toStringCb != null;

        final Map<String, T> reverseMap = new HashMap<>();
        values.forEach(e -> {
            final String k = requireNonNull(toStringCb.apply(requireNonNull(e)));
            if (reverseMap.put(k, e) != null)
                throw new IllegalArgumentException("String function is not one-to-one.");
        });
        return Collections.unmodifiableMap(reverseMap);
    }
}
