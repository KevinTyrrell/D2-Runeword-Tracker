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

package com.kevintyrrell.view;

import com.kevintyrrell.lang.Locale;
import com.kevintyrrell.model.util.CachedValue;
import com.kevintyrrell.model.util.EnumExtendable;

/**
 * ...
 *
 * @since 3.0
 */
public enum Command
{
    ADD("commands/add/command", "commands/add/hint")

    ;

    private final String name;
    private final CachedValue<String> hint;

    Command(final String namePath, final String hintPath)
    {
        assert namePath != null;
        assert hintPath != null;
        name = Locale.get(namePath);
        hint = Locale.get(hintPath);
    }

    /**
     * Extension of the enum, adding additio nal functionality.
     */
    public static final EnumExtendable<Command> extension = new EnumExtendable<>(Command.class);
}
