package com.kevin.tyrrell.diablo.diablo.rune;

import com.kevin.tyrrell.diablo.util.Queryable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines an area where Runewords are loaded, stored, and sorted.
 *
 * @since 3.0
 */
public class RunewordLoader implements Queryable<Runeword>
{
    /* Maps Runeword strings -> Runeword constants -- cannot be read-only in this case. */
    private final Map<String, Runeword> stringMap = new HashMap<>(),
        stringMapRO = Collections.unmodifiableMap(stringMap);

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
        return stringMapRO;
    }
}
