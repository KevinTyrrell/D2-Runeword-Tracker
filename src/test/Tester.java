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

package test;

/*
 * File Name:       Tester
 * File Author:     Kevin Tyrrell
 * Date Created:    07/23/2018
 */

import console.Paragraph;
import console.TextTable;

public final class Tester
{
    private static void test1()
    {
        final Paragraph p = new Paragraph(Paragraph.Alignment.CENTER, "Spineripper",
                "Poignard", "+200-240% Enhanced Damage",
                "Adds 15-27 Damage",
                "15% Increased Attack Speed",
                "+1 To Necromancer Skill Levels",
                "Prevent Monster Heal",
                "Ignore Target's Defense",
                "8% Life Stolen Per Hit",
                "+10 To Dexterity");

        final TextTable t = new TextTable("Name", "Age", "Item?", "Dick Size");
        t.addRow(new Paragraph("Kevin"), new Paragraph("26"), p, new Paragraph("INSANELY LARGE HOLY SHIT"));
        t.addRow(new Paragraph("Black Kevin"), new Paragraph("29"), new Paragraph("None yo"), new Paragraph("SMALL A F"));

        System.out.println(t.toString());
    }

    private static void test2()
    {
        final Paragraph p = new Paragraph(Paragraph.Alignment.CENTER,
                "Meow", "Pancakes and jelly", "WAFFLES");
        final TextTable t = new TextTable("Column 1", "Column 2");
        t.addRow(new Paragraph("Team"), new Paragraph("Mate"));
        t.addRow(new Paragraph(" HOLY HELL BATMAN"), new Paragraph("-"));
        System.out.println(t);
    }

    public static void main(String[] args)
    {
        test1();
    }
}
