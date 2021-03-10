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

import java.util.function.Supplier;

/**
 * Defines an object which constructs an object.
 *
 * @since 3.0
 * @param <T> Type of element in which this builder builds.
 */
public interface Builder<T> extends Supplier<T>
{
    /**
     * Builds a new object based on the current attributes of the builder.
     *
     * @return Constructed object based on the current builder attributes.
     */
    T build();

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override default T get()
    {
        return build();
    }
}
