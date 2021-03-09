/*
 *     TODO: ...
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

import java.io.Serializable;

/**
 * Defines a value which can be cached and recalculated.
 *
 * @since 3.0
 */
public abstract class CachedValue<T> implements Serializable
{
    private T value;

    /* Flag which dictates whether the cached value is invalidated. */
    private static final Object INVALIDATED = new Object();

    /**
     * Recalculates the value, then caches it.
     *
     * @return Recalculated value to be cached.
     */
    protected abstract T recalculate();

    /**
     * Constructs a new cached value.
     */
    public CachedValue()
    {
        invalidate();
    }

    /**
     * Constructs a new cached value with an initial value.
     *
     * @param initialValue Initial value to be cached.
     */
    public CachedValue(final T initialValue)
    {
        value = initialValue;
    }

    /**
     * Invalidate the cached value, forcing it to recalculate later.
     */
    @SuppressWarnings("unchecked")
    public void invalidate()
    {
        value = (T)INVALIDATED; // Flag data as invalidated.
    }

    /**
     * Retrieves the cached value.
     *
     * If the value was flagged as invalidated, it is
     * recalculated and re-cached before being returned.
     *
     * @return Cached value or re-calculated value.
     */
    public T get()
    {
        if (value == INVALIDATED)
            value = recalculate();
        return value;
    }
}
