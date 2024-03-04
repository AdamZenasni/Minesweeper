package org.example;

public class DisplayBoard {

    static void displayBoard(Cell[][] board) {
        // Display column numbers above the board with dots within columns
        System.out.print("     ");
        for (int j = 0; j < board.length; j++) {
            System.out.printf("[ %2d ] ", j);
        }
        System.out.println();

        for (int i = 0; i < board.length; i++) {
            System.out.printf("[ %2d ] ", i);

            for (int j = 0; j < board.length; j++) {
                if (board[i][j].isRevealed) {
                    if (board[i][j].isFlagged) {
                        System.out.print(" F     ");
                    } else if (board[i][j].isMine & !board[i][j].isFlagged) {
                        System.out.print(" B     ");
                    } else if (board[i][j].hasNumbers) {
                        System.out.printf(" %d     ", board[i][j].mineCounter);
                    } else {
                        System.out.print(" E     ");
                    }
                } else {
                    if (board[i][j].isFlagged) {
                        System.out.print(" F     ");
                    } else {
                        System.out.print(" •     ");
                    }
                }
            }
            System.out.println();
        }
    }
}