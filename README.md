## Diablo 2 Runeword Tracker

Console program which helps players understand how close they are to completing Runewords. Uses documented drop statistics to get far more accurate estimates. Runs completely seperate from Diablo 2 and thus all runes you obtain or have obtained must be entered in manually. The tracker outputs Runewords that you can make in a sorted fashion; from the most difficult to make to the most trivial. Runewords that you don't care for can be ignored and won't be listed. The same can be done for item slots. Your rune library and ignoring preferences are saved automatically as you enter in data.

#### Prerequisites

The following are required:

* [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) *(9.0.4 or higher)*

*You can test what your Java version is by doing the following:*

Ctrl+R -> cmd.exe -> `java --version`

#### Installing

Visit the **[Releases](https://github.com/KevinTyrrell/D2-Runeword-Tracker/releases)** section of the repo or click [here](https://github.com/KevinTyrrell/D2-Runeword-Tracker/releases/latest) for the latest version.

###### Windows

Either double click the `D2-Runeword-Tracker.bat` file or open up a console window in the directory you saved the release and type `java -jar D2-Runeword-Tracker.jar`.

###### Linux / Mac

Open a console window in the directory you saved the release and type `java -jar D2-Runeword-Tracker.jar`.

----
Save files for Diablo 2 Runeword Tracker will be created in the directory the `jar` is located.

#### General Use

Once the program is open, you should begin adding the runes you have in-game to the program. To add runes, type the `add` command followed by a space separated list of runes. In my game, I have an Ort, Tal, and Tir rune, so I will type `add ort tal tir`. 

![Img](https://i.imgur.com/zZ5vGAa.png "Adding Runes")

Based on what runes you have, various runewords will be displayed.

![Img](https://i.imgur.com/VypktlG.png "Basic output")

Ort, Tir, and Tal are all used for a lot of words, so I get a lot of results. Some runewords are omitted because I don't have enough of its runes for it to be considered worth tracking at the moment. Let's break down everything that's shown here:

|            |                                                                           |
|------------|---------------------------------------------------------------------------|
| Runeword   | The formal name of the Runeword                                           |
| Rank       | How difficult the Runeword is to make. Breath of the Dying is #1.         |
| Completion | How close you are to completing the Runeword with known drop statistics.  |
| Word       | The order in which you place the runes into the base item.                |
| Base(s)    | The bases that the Runeword is for, excluding those which you've ignored. |

Why is Lore at 28% completion? I have half of the runes needed to make it. Doesn't that mean I'm 50% of the way there? Not quite. According to [the data provided by a user named Urlik](https://diablo2.diablowiki.net/Guide:Rune_Finder_Guide_v1.10,_by_Urlik) the chances of an Ort rune dropping is `48885` out of `1,000,000` rune drops. The Sol rune is a bit more uncommon, specifically `19916/1,000,000`; about twice as rare. This is why the final percentage of completion is ~28%. 

Let's say in-game, I were to sell some runes. I'll need to remove the rune(s) from the program as well. Removing runes is the same syntax as adding, except we will use the `toss` command.

```
toss tir
```

Not every runeword may be of interest to you. Or, perhaps you're a melee character and have no desire for Runewords that only go into bows or crossbows. In that case, we can use the `ignore` command to ignore any number of Runewords or Item types from being printed in the runeword chart. For runewords or types with multiple words, use `_` instead of a space. Ignore any other symbols in names. For example to ignore the *"Ancient's Pledge"* runeword, I would type `ignore ancients_pledge`.

```
ignore steel bow nadir crossbow helm body_armor
```

![Img](https://i.imgur.com/kuXqVyi.png "Tossing and ignoring")

To un-ignore a runeword or item type, type `ignore` just like before and it will toggle the runeword or item type back to being tracked. To erase all tracked item types or runewords, you can also close the program, delete the `IgnoredTypes.ser` and `IgnoredWords.ser` files respectively, and re-start the program.

![Img](https://i.imgur.com/ADspu0k.png "Completion")

Adding a Ral rune completes the runeword called *Ancient's Pledge*. you can see the program indicating that I have `100.00%` completion of it. If I were to make the Runeword, I would then `toss ort ral tal`.

#### Contact

Feel free to email me with any questions, comments, or concerns at `kev070892@gmail.com`.

#### License

This project is licensed under the MIT License.