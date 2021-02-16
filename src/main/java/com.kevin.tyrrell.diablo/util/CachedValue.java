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
    abstract T recalculate(final T oldValue);

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
     * If the value was invalidated,
     * it is recalculated and re-cached.
     *
     * @return Cached value.
     */
    public T get()
    {
        if (invalidated)
        {
            value = recalculate();
            invalidated = false;
        }

        return value;
    }
}
