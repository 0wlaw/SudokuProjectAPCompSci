# SudokuProjectAPCompSci
Oliver Lawson

This project successfully generates a completely random sudoku board, and then exhibits a gameplay loop where the user is given feedback on the validity of their moves, until the user successfully completes the board.

Run the code using a java compiler. The prompts in the terminal will guide you, and the board is displayed in the terminal as well.

The Sudoku board is generated using a recursive method. On each call, the method picks a random legal guess for an empty square, but calls itself and only keeps this choice (and returns true) if itself returns true. The base case is when the board is completed---so if the recursive method reaches a dead end where there are no valid guesses, then it returns false and removes the previous guess because it has no solutions. It would then guess another value for that cell and continue. 

DesignDoc.docx        |  Details design process
README.md             |  Brief description
SudokuGenerator.java  |  Only Java class
                      |
