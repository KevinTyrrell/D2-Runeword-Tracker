/*
Copyright © 2018 Kevin Tyrrell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package diablo;

/*
 * File Name:       Version
 * File Author:     Kevin Tyrrell
 * Date Created:    07/17/2018
 */

import java.time.LocalDate;

public final class Version
{
    private static final int MAJOR = 2,     /* Non-backwards compatible changes are made. */
        MINOR = 1,                          /* Backwards compatible features implemented. */
        PATCH = 1;                          /* Backwards compatible bug fixes implemented. */
    
    /* Version format for String representations. */
    private static final String FORMAT = "v%d.%d.%d";

    /**
     * Diablo 2 Runeword Tracker semantic version number.
     */
    public static final String NUMBER = String.format(FORMAT, MAJOR, MINOR, PATCH);

    /**
     * Date on which the release was compiled.
     */
    public static final LocalDate COMPILED_ON = LocalDate.of(2018, 7, 26);
}
