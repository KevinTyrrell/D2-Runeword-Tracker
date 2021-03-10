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

package com.kevintyrrell.view;

import java.util.Collections;

import static java.util.Collections.nCopies;
import static java.lang.String.join;

/**
 * ...
 *
 * TODO: \n Newline imposes a considerable challenge.
 * TODO: Newline should be processed at the very end.
 * TODO: It poses a problem when counting the number
 * TODO: of characters OR when it disrupts text alignment.
 *
 * @since 3.0
 */
public final class ParagraphBuilder
{
    private Alignment alignment = Alignment.LEFT;

    enum Alignment
    {
        LEFT, CENTER, RIGHT, JUSTIFIED;
    }
}
