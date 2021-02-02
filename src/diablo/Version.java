/*
 * Application which tracks Runeword progress in the video game Diablo 2.
 * Copyright (C) 2018  Kevin Tyrrell
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

package diablo;

import java.time.LocalDate;

/**
 * Program version information.
 *
 * TODO: Move to Utilities package.
 *
 * @since 2.0
 */
public final class Version
{
    private static final int MAJOR = 2,     /* Non-backwards compatible changes are made. */
        MINOR = 1,                          /* Backwards compatible features implemented. */
        PATCH = 5,                          /* Backwards compatible bug fixes implemented. */
        COMPILE_MONTH = 2,                  /* Month of the date in which release was compiled. */
        COMPILE_DAY = 2,                    /* Day of the date in which release was compiled. */
        COMPILE_YEAR = 2021;                /* Year of the date in which release was compiled. */
    
    /* Version format for String representations. */
    private static final String FORMAT = "v%d.%d.%d";

    /**
     * Diablo 2 Runeword Tracker semantic version number.
     */
    public static final String NUMBER = String.format(FORMAT, MAJOR, MINOR, PATCH);

    /**
     * Date on which the release was compiled.
     */
    public static final LocalDate COMPILED_ON = LocalDate.of(2018, 8, 1);
}
