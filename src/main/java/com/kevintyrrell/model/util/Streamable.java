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

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Defines a standardized way of streaming non-collections.
 *
 * This class is somewhat similar to the spring framework counterpart:
 *  https://github.com/spring-projects/spring-data-commons/blob/master/src/main/java/org/springframework/data/util/Streamable.java
 *
 * @since 3.0
 */
public interface Streamable<T> extends Supplier<Stream<T>>
{
    /**
     * Streams over each element of the streamable.
     *
     * Typically the stream will originate via Collections#stream.
     *
     * @return Stream of the streamable.
     * @see Collection#stream()
     */
    Stream<T> stream();

    /**
     * @param stream Stream to flatten.
     * @return Flattened stream.
     * @see Stream#flatMap(Function)
     */
    default Stream<T> flatMap(final Stream<T> stream)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override default Stream<T> get()
    {
        return stream();
    }
}
