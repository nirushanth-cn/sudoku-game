# 🧩 Java Sudoku Console Game

A simple **console-based Sudoku game** written in Java.
It generates a valid Sudoku puzzle, allows user interaction, provides hints, and validates moves.

---

## 🚀 Features

* ✅ Random Sudoku board generation (valid solution using backtracking)
* 🎮 Interactive CLI gameplay
* 🧠 Hint system (reveals a correct value)
* ✔️ Move validation (row, column, subgrid checks)
* 🔄 Ability to clear and re-enter values
* 🏁 Completion detection

---

## 🏗️ Project Structure

### `SudokuService`

All the game logics are handled in the SudokuGameApplication.java class and the startGame() method is invoked from the SudokuGameApplication.java main class

### Boards Used

* `board_filled` → Complete solved Sudoku
* `board_template` → Puzzle with blanks
* `board_game` → Current player state

---

## ▶️ How to Run
Open the source code in any Java integrated IDE, simply run the main method of SudokuGameApplication.java file


## 🎮 How to Play

When the game starts, you’ll see a Sudoku grid like:

```
   1 2 3   4 5 6   7 8 9
A  - - - | - - - | - - -
B  - - - | - - - | - - -
...
```

---

## ⌨️ Commands

| Command    | Description                       |
| ---------- | --------------------------------- |
| `A3 4`     | Insert value `4` at position `A3` |
| `C5 clear` | Clear value at position `C5`      |
| `hint`     | Get a random correct cell value   |
| `check`    | Validate last move                |
| `quit`     | Exit the game                     |

---

## 🧠 Rules Enforced

* No duplicate numbers in:

    * Row
    * Column
    * 3×3 subgrid
* Cannot modify pre-filled cells
* Values must be between **1–9**

---

## 🏁 Winning

You win when:

```
board_game == board_filled
```

The program will automatically detect completion and restart the game.

---

## ⚠️ Notes

* Input is **case-insensitive** (`a1 5` works)
* Invalid commands are safely handled
* Game runs entirely in the console

---

## 🔧 Possible Improvements

* Difficulty levels (easy / medium / hard)
* GUI version (Swing / JavaFX)
* Timer & scoring system

---

## 👨‍💻 Author
NIRUSHANTH CN

Enjoy playing Sudoku! 🎉
