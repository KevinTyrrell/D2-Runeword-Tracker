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

    /* Flag which dictates whether the cached value is invalidated. */
    private static final Object INVALIDATED = new Object();

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
            value = recalculate(value);
        return value;
    }
}
