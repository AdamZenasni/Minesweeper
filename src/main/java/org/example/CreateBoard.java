package org.example;

import java.util.Random;

public class CreateBoard {

    public static Cell[][] createBoard(int size) {

        Cell[][] board = new Cell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Cell(); // creating a cell class for every co-ordinate that will have the same fundamental details.
            }
        }

        return board;
    }

    public static void placeBombs(int size, int numBombs, Cell[][] board, int firstMoveX, int firstMoveY) {

        Random random = new Random();
        int bombsPlaced = 0;
        while (bombsPlaced < numBombs) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            if (!board[x][y].isMine && !((x == firstMoveX || x == firstMoveX - 1 || x == firstMoveX + 1) &&
                    (y == firstMoveY || y == firstMoveY - 1 || y == firstMoveY + 1))) {
                board[x][y].isMine = true;
                bombsPlaced++;
            }
        }
    }
}
