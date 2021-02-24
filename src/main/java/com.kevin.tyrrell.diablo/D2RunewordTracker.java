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

package com.kevin.tyrrell.diablo;

import java.time.LocalDate;

/**
 * Application entry point.
 *
 * @since 2.0
 */
public final class D2RunewordTracker
{
    public static void main(final String[] args)
    {
    }

    /**
     * Version and compilation information.
     */
    public static final Version VERSION = Version.INSTANCE;

    private enum Version
    {
        INSTANCE;

        private static final int
                MAJOR = 3,              /* Non-backwards compatible changes are made. */
                MINOR = 0,              /* Backwards compatible features implemented. */
                PATCH = 0,              /* Backwards compatible bug fixes implemented. */
                COMPILE_MONTH = 2,      /* Month of the date in which release was compiled. */
                COMPILE_DAY = 24,       /* Day of the date in which release was compiled. */
                COMPILE_YEAR = 2021;    /* Year of the date in which release was compiled. */

        /* Version format for String representations. */
        private static final String VERSION_FORMAT = "v%d.%d.%d";

        /**
         * Semantic version number.
         */
        public final String NUMBER = String.format(VERSION_FORMAT, MAJOR, MINOR, PATCH);

        /**
         * Date on which the release was compiled.
         */
        public final LocalDate COMPILED_ON = LocalDate.of(COMPILE_YEAR, COMPILE_MONTH, COMPILE_DAY);

        /**
         * @return String representation of the version.
         */
        @Override public String toString()
        {
            return NUMBER;
        }
    }
}
