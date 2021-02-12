/*
 * Application which tracks Runeword progress in the video game Diablo 2.
 * Copyright (C) 2018  Kevin Tyrrell
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

package util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Extension for Enumerations, adding small extra functionality.
 *
 * @since 2.0
 */
public interface EnumExtendable<T extends Enum<?>>
{
    /**
     * Read-only list of all values in the enum.
     *
     * The method should only be implemented by returning a
     * private class member List<T> created by `createEnumList`.
     *
     * @see EnumExtendable#createEnumList(Enum[])
     * @return read-only list of all values in the enum.
     */
    List<T> values();

    /**
     * Read-only map which associates string representations of all
     * enum values in the enum to their respective enum values.
     *
     * Overriding this method is optional and should only be performed
     * if enum value lookups from their string representations are needed.
     *
     * By default, map keys are set to enum.toString(). This behavior
     * can be changed by calling the overloaded function `createStringMap`.
     *
     * The method should only be implemented by returning a
     * private class member Map<T, E> created by `createStringMap`.
     *
     * @see EnumExtendable#createStringMap(List, Function)
     * @see EnumExtendable#fromString(String)
     * @return read-only map associating enum string representations to enum values.
     */
    default Map<String, T> stringMap()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the number of elements in the enum.
     *
     * With normal enums this operation can only be performed by calling:
     *  `values().length`, which has a linear overhead of complexity and storage.
     *  or manually referencing the last enum element and calling ordinal(),
     *  which is bug prone when enum values are shifted in order/removed/added.
     *
     * @return number of elements in the enum.
     */
    default int size()
    {
        return requireNonNull(values()).size();
    }

    /**
     * Retrieves an enum value based its respective ordinal value.
     * Enum ordinal values start at 0, increase sequentially, and
     * are assigned in the order of which the enum values are defined.
     *
     * @see Enum#ordinal()
     * @param ordinal Ordinal to query.
     * @return Enum element associated with the ordinal value.
     */
    default T fromOrdinal(final int ordinal)
    {
        final List<T> list = requireNonNull(values());
        if (ordinal < 0 || ordinal >= requireNonNull(list).size())
            throw new IndexOutOfBoundsException(ordinal);
        return list.get(ordinal);
    }

    /**
     * Retrieves an enum value based on its respective string representation.
     *
     * By default, map keys are set to enum.toString(). This behavior
     * can be changed by calling the overloaded function `createStringMap`.
     *
     * @see EnumExtendable#createStringMap(List, Function)
     * @see EnumExtendable#stringMap()
     * @param str String representation of the desired enum value.
     * @return Enum value corresponding to the string, or null.
     */
    default T fromString(final String str)
    {
        final Map<String, T> reverseMap = requireNonNull(stringMap());
        return reverseMap.get(requireNonNull(str));
    }

    /**
     * Creates an immutable list from all the values in an Enum.
     * Designed to be used in conjunction with `values()`.
     *
     * EnumExtendable objects should have a private member List<T>,
     * which is initialized as the return value of this function.
     * The member should then be used as the return value of `values()`.
     *
     * @see EnumExtendable#values()
     *
     * EnumExtendables should create a private List<T> which
     * is assigned to the return value of this function.
     *
     * `values()` should be passed as the parameter.
     *
     * Returned list should be used as the return value of `values()`.
     * `createEnumList` should only be called once per EnumExtendable.
     *
     * @see EnumExtendable#values()
     * @param values Values of the Enum, preferably from a static `values()` call.
     * @param <T> Enum data type.
     * @return immutable list of values in the enum, preferably used for `values()`.
     */
    static <T extends Enum<T>> List<T> createEnumList(final T[] values)
    {
        assert values.length > 0; // Not sure if this can happen.
        return Arrays.stream(requireNonNull(values))
                .distinct()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(), Collections::unmodifiableList));
    }

    /**
     * Creates an immutable map from all values in an Enum.
     *
     * The parameter list of values should be immutable,
     * created initially from the `createEnumList` function.
     *
     * Map keys are assigned from the result of the callback function for each value.
     *
     * Returned map should be used as the return value of `stringMap()`.
     * `createStringMap` should only be called once per EnumExtendable.
     *
     * @param values Values of the enum, preferably from a static `createEnumList()` call.
     * @param toStringCb Callback associating enum values with their string representations.
     * @param <T> Data type of the enum.
     * @return Map associating string representations with their respective enum values.
     */
    static <T extends Enum<T>> Map<String, T> createStringMap(final List<T> values,
                                                              final Function<T, String> toStringCb)
    {
        return toReverseMap(requireNonNull(values), requireNonNull(toStringCb));
    }

    /**
     * Creates an immutable map from all values in an Enum.
     *
     * The parameter list of values should be immutable,
     * created initially from the `createEnumList` function.
     *
     * Map keys are assigned from the result of the callback function for each value.
     *
     * Returned map should be used as the return value of `stringMap()`.
     * `createStringMap` should only be called once per EnumExtendable.
     *
     * By default, map keys are set to enum.toString(). This behavior
     * can be changed by calling the overloaded function `createStringMap`.
     *
     * @see #createStringMap(List, Function)
     * @param values Values of the enum, preferably from a static `createEnumList()` call.
     * @param <T> Data type of the enum.
     * @return Map associating string representations with their respective enum values.
     */
    static <T extends Enum<T>> Map<String, T> createStringMap(final List<T> values)
    {
        return toReverseMap(requireNonNull(values), Enum::toString);
    }

    private static <T extends Enum<T>> Map<String, T> toReverseMap(final List<T> values,
                                                                   final Function<T, String> toStringCb)
    {
        assert values != null;
        assert toStringCb != null;
        final Map<String, T> reverseMap = new HashMap<>(values.size());
        for (final T value : values)
        {
            final String k = requireNonNull(toStringCb.apply(requireNonNull(value)));
            if (reverseMap.put(k, value) != null)
                throw new IllegalArgumentException("String function is not one-to-one.");
        }

        return Collections.unmodifiableMap(reverseMap);
    }
}
