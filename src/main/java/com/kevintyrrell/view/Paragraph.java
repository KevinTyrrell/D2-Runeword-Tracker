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

import java.util.List;

/**
 * Immutable multi-line String which can be aligned in a multitude of ways.
 *
 * @since 2.0
 */
public class Paragraph
{


    /* Immutable list of each line of the paragraph. */
    private final List<String> lines;
    /* Maximum width of the paragraph. */
    private final int width;

    public Paragraph(final String content, final int width, final Alignment alignment)
    {
        this.lines = null;
        this.width = 0;
    }
}
