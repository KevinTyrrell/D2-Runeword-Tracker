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

package console;

/*
 * File Name:       TextTable
 * File Author:     Kevin Tyrrell
 * Date Created:    07/23/2018
 */

import util.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TextTable
{
    /* Each row of the table, from top to bottom. */
    private final List<List<Paragraph>> rows = new ArrayList<>();

    /* Character that make up the border of the table. */
    private static final char HORZ_BORDER_CHAR = '.';

    public TextTable(final String... columnNames)
    {
        assert columnNames.length > 0;

        rows.add(Arrays.stream(columnNames)
                .map(c -> new Paragraph(Paragraph.Alignment.CENTER, c))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList)));
    }

    /**
     * Appends a new row at the bottom of the table.
     * @param paragraphs Paragraphs of the row.
     * @return this.
     */
    public TextTable addRow(final Paragraph... paragraphs)
    {
        assert paragraphs.length == rows.get(0).size();

        rows.add(Arrays.stream(paragraphs)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList)));
        return this;
    }

    /**
     * @return String representation of the table.
     */
    @Override
    public String toString()
    {
        final int COLUMNS = rows.get(0).size();
        final int ROWS = rows.size();
        
        /* Find the largest widths of every column. */
        final int[] maxColumnWidths = IntStream.range(0, COLUMNS)
                .map(i -> rows.stream()
                        .mapToInt(row -> row.get(i).getWidth())
                        .max().getAsInt())
                .toArray();
        /* Inform each Paragraph to grow to a new width. */
        IntStream.range(0, COLUMNS)
                .forEach(i -> rows.stream()
                        .map(row -> row.get(i))
                        .forEach(p -> p.setWidth(maxColumnWidths[i])));

        final int maxWidth = IntStream.range(0, COLUMNS)
                .map(col -> rows.stream()
                        .mapToInt(row -> row.get(col).getWidth())
                        .max().getAsInt())
                .sum() + 1 + 3 * COLUMNS;
        /* Horizontal border between rows. */
        final String div = "\n".concat(Utilities.repeatChar(HORZ_BORDER_CHAR, maxWidth)).concat("\n");

        /* Determine the most amount of lines are in every row. */
        final int[] maxRowHeights = rows.stream()
                .mapToInt(row -> IntStream.range(0, COLUMNS)
                        .map(j -> row.get(j).getLines())
                        .max().getAsInt())
                .toArray();

        /* Go through every row... */
        return IntStream.range(0, ROWS)
                /* Go through every line of each row... */
                .mapToObj(row -> IntStream.range(0, maxRowHeights[row])
                        /* Go through every line of every column of each row... */
                        .mapToObj(line -> IntStream.range(0, COLUMNS)
                            .mapToObj(col ->
                            {
                                final Paragraph p = rows.get(row).get(col);
                                if (line < p.getLines())
                                    return p.getLine(line);
                                else
                                    return Utilities.repeatChar(' ', maxColumnWidths[col]);
                            })
                            .collect(Collectors.joining(" | ", "| ", " |")))
                        .collect(Collectors.joining("\n")))
                .collect(Collectors.joining(div, div, div));
    }
}