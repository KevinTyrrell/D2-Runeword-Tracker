## Diablo 2 Runeword Tracker

Console application which helps Diablo 2: Lord of Destruction players visualize what Runeword(s) they can create. As the user collects Runes in-game and inputs them into the program, D2 Runeword Tracker will output an organized table of possible Runeword candidates that either can be made or are on-their-way to being completed. A weighted sum algorithm which takes into account documented Rune drop statistics is used to calculate incredibly accurate estimates of how close the player is to assembling all of the Runes needed for a given Runeword.

D2 Runeword Tracker will automatically hide Runewords which it deems the player is nowhere near completing. In addition the user can also request the program hides specific Runewords or Runewords for a particular Item slot. The tracker will indicate when certain Runes are no longer beneficial to you and should be thrown out. Simply collect Runes in-game, enter them into the tracker, and let the program help plan your character.

### Prerequisites

The following are required:

* [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) *(9.0.4 or higher)*

*You can test what your Java version is by doing the following:*

Ctrl+R -> cmd.exe -> `java --version`

### Installing

Visit the **[Releases](https://github.com/KevinTyrrell/D2-Runeword-Tracker/releases)** section of the repo or click [here](https://github.com/KevinTyrrell/D2-Runeword-Tracker/releases/latest) for the latest version.

##### Windows

Either double click the `D2-Runeword-Tracker.bat` file or open up a console window in the directory you saved the release and type `java -jar D2-Runeword-Tracker.jar`.

##### Linux / Mac

Open a console window in the directory you saved the release and type `java -jar D2-Runeword-Tracker.jar`.

----
Save files for Diablo 2 Runeword Tracker will be created in the directory the `jar` is located.

### General Use

Once the program is open, you should begin adding the Runes you have in-game to the program. To add Runes, type the `add` command followed by a space separated list of Runes. In my game, I have an Ort, Tal, and Tir Rune, so I will type `add ort tal tir`. 

![Img](https://github.com/KevinTyrrell/D2-Runeword-Tracker/raw/master/res/TutorialImg1.png "Adding Runes")

Based on what Runes you have, various Runewords will be displayed.

![Img](https://github.com/KevinTyrrell/D2-Runeword-Tracker/raw/master/res/TutorialImg2.png "Runeword output")

Ort, Tir, and Tal are all used for a lot of words, so I get a lot of results. Some Runewords are omitted because I don't have enough of its significant Runes for the program to consider it being worth tracking at the moment. Let's break down everything that's shown here:

|            |                                                                             |
|------------|-----------------------------------------------------------------------------|
| Runeword   | The formal name of the Runeword with its level requirement.                 |
| Rank       | How difficult the Runeword is to make. Breath of the Dying is #1.           |
| Completion | How close you are to completing the Runeword with known drop statistics.    |
| Word       | The order in which you place the Runes into the base item.                  |
| Base(s)    | The base(s) that the Runeword is for, excluding those which you've ignored. |

Why is Lore at 28% completion? I have half of the Runes needed to make it. Doesn't that mean I'm 50% of the way there? Not quite. According to [the data provided by a user named Urlik](https://diablo2.diablowiki.net/Guide:Rune_Finder_Guide_v1.10,_by_Urlik) the chances of an Ort Rune dropping is `48885` out of `1,000,000` Rune drops. The Sol Rune is a bit more uncommon, specifically `19916/1,000,000`; about twice as rare. This is why the final percentage of completion is ~28%. 

Let's say in-game, I were to sell some Runes. I'll need to remove the Rune(s) from the program as well. Removing Runes is the same syntax as adding, except we will use the `toss` command.

```
toss tir
```

Not every Runeword may be of interest to you. Or, perhaps you're a melee character and have no desire for Runewords that only go into bows or crossbows. In that case, we can use the `ignore` command to ignore any number of Runewords or Item types from being printed in the Runeword chart. For Runewords or types with multiple words, use `_` instead of a space. Ignore any other symbols in names. For example to ignore the *"Ancient's Pledge"* Runeword, I would type `ignore ancients_pledge`.

```
ignore steel bow nadir crossbow helm body_armor
```

![Img](https://github.com/KevinTyrrell/D2-Runeword-Tracker/raw/master/res/TutorialImg3.png "Tossing and ignoring")

To un-ignore a Runeword or item type, type `ignore` just like before and it will toggle the Runeword or item type back to being tracked. To erase all tracked item types or Runewords, you can also close the program, delete the `IgnoredTypes.ser` and `IgnoredWords.ser` files respectively, and re-start the program.

![Img](https://github.com/KevinTyrrell/D2-Runeword-Tracker/raw/master/res/TutorialImg4.png "Completing Runewords")

Adding a Ral Rune completes the Runeword called *Ancient's Pledge*. you can see the program indicating that I have `100.00%` completion of it. If I were to make the Runeword, I would then `toss ort ral tal`.

The most sophisticated feature thus far is the **insignificant Runes** warning. The program will warn you of Runes that you own which are not contributing much (or at all) to a tracked Runeword. Here's an example:

![Img](https://github.com/KevinTyrrell/D2-Runeword-Tracker/raw/master/res/TutorialImg5.png "Insignificant Runes")

Our Rune library currently consists of a Zod, Vex, Lum, and El rune. The program indicates that Lum and El do not contribute towards a tracked goal. Lum actually DOES contribute to a goal, specifically towards the Runeword Splendor (which we have 95% completed). However, I previously ignored the runeword Splendor, and thus Lum has no contribution to any Runeword we have listed above and should be sold. The El rune has a different scenario. It is contributing to the Runeword Breath of the Dying, but because El is commonly found, the program will indicate that it is not worth holding onto until we are closer to completing the Runeword. In this case, collecting a Hel rune removes the warning, since we are now close enough to completing Breath of the Dying to make holding onto an El Rune worth the hassle.

For more information about how the insignificant Rune system works, you can look into the [source code here](https://github.com/KevinTyrrell/D2-Runeword-Tracker/blob/master/src/diablo/RuneLibrary.java#L105).

### Contact

Feel free to email me with any questions, comments, or concerns at `kev070892@gmail.com`.

### License

This project is licensed under the MIT License.