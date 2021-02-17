package com.kevin.tyrrell.diablo.util;

import java.io.Serializable;

/**
 * Value which can be cached and recalculated.
 *
 * @since 3.0
 */
public abstract class CachedValue<T> implements Serializable
{
    private T value;
    private transient boolean invalidated;

    /**
     * Recalculates the value, then caches it.
     *
     * @param oldValue Current value to be recalculated.
     * @return Recalculated value to be cached.
     */
    protected abstract T recalculate(final T oldValue);

    /**
     * Constructs a new cached value.
     */
    public CachedValue()
    {
        value = null;
        invalidated = true;
    }

    /**
     * Constructs a new cached value with an initial value.
     *
     * @param initialValue Initial value to be cached.
     */
    public CachedValue(final T initialValue)
    {
        value = initialValue;
        invalidated = false;
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
        if (invalidated)
        {
            value = recalculate(value);
            invalidated = false;
        }

        return value;
    }
}
