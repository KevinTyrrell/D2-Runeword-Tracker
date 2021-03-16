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

package com.kevintyrrell.view.console;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Defines a
 *
 * @since 3.0
 */
public class Console
{
    /* Expected pattern to split the input on. */
    final Pattern inputPattern = Pattern.compile("\\s*\\w\\s*");

    private final Scanner sc = new Scanner(System.in);
}
