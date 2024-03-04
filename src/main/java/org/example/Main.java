package org.example;

import java.util.*;

import static org.example.DisplayBoard.displayBoard;

public class Main {

    // cannot take away flags
    // can place flag on every cell and then win - what i need to do is introduce a flagsplaced and then
    // if flag is placed in mine then it should be checked against it whether a bomb is there or not

    // need to have three levels of difficulty - DONE
    // can not start on a bomb - DONE
    // flagging - DONE
    // validate user input - DONE
    // formatting of the board - DONE
    // split into classes


    public void minesweeperGame() {
        Scanner reader = new Scanner(System.in);
        boolean gameIsOver = false;
        System.out.println("WELCOME TO MINESWEEPER");

        System.out.println("Please enter the difficulty level or choose custom settings:");
        System.out.println("1. Easy (10x10 board, 10 bombs)");
        System.out.println("2. Medium (15x15 board, 40 bombs)");
        System.out.println("3. Hard (20x20 board, 80 bombs)");
        System.out.println("4. Custom");

        int choice = 0;
        while (choice < 1 || choice > 4) {
            System.out.print("Enter your choice: ");
            choice = reader.nextInt();
            reader.nextLine();
            if (choice < 1 || choice > 4) {
                System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }

        int boardSize = 0;
        int numBombs = 0;
        switch (choice) {
            case 1:
                boardSize = 10;
                numBombs = 10;
                break;
            case 2:
                boardSize = 15;
                numBombs = 40;
                break;
            case 3:
                boardSize = 20;
                numBombs = 80;
                break;
            case 4: // Need to ensure they cannot put a board higher than 50 and more bombs than cells
                boolean validSize = false;
                while (!validSize) {
                    try {
                        System.out.print("Enter board size (e.g., 10 for a 10x10 board, maximum is 50): ");
                        boardSize = reader.nextInt();
                        if (boardSize > 0 && boardSize <= 50) {
                            validSize = true;
                        } else {
                            System.out.println("Invalid board size. Please enter a number between 1 and 50.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        reader.nextLine();
                    }
                }
                boolean validBombs = false;
                while (!validBombs) {
                    try {
                        System.out.print("Enter number of bombs (maximum is " + boardSize + "): ");
                        numBombs = reader.nextInt();
                        if (numBombs > 0 && numBombs <= boardSize) {
                            validBombs = true;
                        } else {
                            System.out.println("Invalid number of bombs. Please enter a number between 1 and " + boardSize + ".");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        reader.nextLine();
                    }
                }
                break;
        }

        // create the board template
        Cell[][] board = createBoard(boardSize);

        displayBoard(board); // display the board

        // ensure the first guess cannot land on a bomb
        int[] firstGuess = getCoordinates(reader, "Please enter your coordinates (either as x y or x,y): ", boardSize);
        int firstX = firstGuess[0];
        int firstY = firstGuess[1];

        board[firstX][firstY].isRevealed = true;
        placeBombs(boardSize, numBombs, board, firstX, firstY);
        countNeighboringMines(board);
        board = sweep(board, firstX, firstY);
        displayBoard(board);
        gameIsOver = checkIfGameIsWon(board);

        // NOW I NEED TO TAKE USER INPUT. And split it into x and y variables to check the list

        while (!gameIsOver) {
            int[] userGuess = getCoordinates(reader, "Please enter your coordinates (either as x y or x,y): ", boardSize);
            int x = userGuess[0];
            int y = userGuess[1];
            boolean correctChoice = false;

            if (board[x][y].isRevealed && !board[x][y].isFlagged) { // if the cell is revealed
                System.out.println("Cell already revealed. Choose another one."); // is blocked.
            } else {
                try {
                    while (!correctChoice) {
                        System.out.print("Do you want to flag this cell? (Y/N) Or would you like to unflag this cell? (U): ");
                        String flagChoice = reader.nextLine().toUpperCase();
                        if (flagChoice.equals("Y")) {
                            board[x][y].isFlagged = true;
                            correctChoice = true;
                        } else if (flagChoice.equals("U")) {
                            if (board[x][y].isFlagged) {
                                board[x][y].isFlagged = false;
                                correctChoice = true;
                            } else {
                                System.out.println("No flag to remove. Please enter your coordinates again.");
                            }
                        } else if (flagChoice.equals("N")) {
                            break;
                        } else {
                            System.out.println("Invalid Input. Please enter either Y (for Yes) or N (for No)");
                            reader.nextLine();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Invalid coordinates. Please enter valid coordinates.");
                    continue;
                }
                // If the selected cell is empty, reveal it and its neighbors
                if (board[x][y].mineCounter == 0) { // if it doesn't have any neighboring bombs.
                    board = sweep(board, x, y); // now we need to reveal as much of the board as possible
                } else {
                    board[x][y].isRevealed = true;
                }

                if (board[x][y].isMine) { // if they have hit a bomb
                    if (board[x][y].isFlagged) { //
                        displayBoard(board);
                    } else {
                        System.out.println("Unlucky, you have hit a bomb");
                        gameIsOver = true;
                    }
                } else {
                    countNeighboringMines(board); // count neighboring mines and update the display
                    displayBoard(board);

                    // Check if the game is won after updating the display
                    gameIsOver = checkIfGameIsWon(board);
                }
            }
        }
    }


    private int[] getCoordinates(Scanner reader, String message, int boardSize) {
        int[] intCoordinates = new int[2]; // create an array
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print(message);
                String userGuess = reader.nextLine();
                String[] stringCoordinates = userGuess.split("[,\\s]+");

                System.out.println(Arrays.toString(stringCoordinates));

                if (stringCoordinates.length != 2) {
                    System.out.println("Invalid coordinates. Please enter two numbers separated by either a space or a comma.");
                    continue;
                }

                int x = Integer.parseInt(stringCoordinates[0]); // convert the strings into int
                int y = Integer.parseInt(stringCoordinates[1]);

                // making sure x and y are within the board
                if (x < 0 || x >= boardSize || y < 0 || y >= boardSize) {
                    System.out.println("Coordinates out of bounds. Please enter coordinates within the range (0-" + (boardSize - 1) + ").");
                    continue;
                }

                intCoordinates[0] = x; // put x and y into an array of ints
                intCoordinates[1] = y;
                validInput = true;
            } catch (Exception e) {
                System.out.println("Invalid coordinates. Please enter two numbers separated by either a space or a comma.");
            }
        }
        return intCoordinates;
    }

    public Cell[][] createBoard(int size) {

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

    public static Cell[][] sweep(Cell[][] board, int x, int y) { // reveal all the cells with numbers
        board[x][y].isRevealed = true;
        if (board[x][y].isFlagged) {
            return board;
        }
        List<int[]> neighbours = getNeighboringCells(board, x, y); // create an array of neighbouring cells to the one revealed
        if (neighbours.isEmpty()) {
            return board; // if there is no more to check return board
        }
        for (int[] cell : neighbours) { //for every cell in the list
            board[cell[0]][cell[1]].isRevealed = true; // reveal the neighbouring cell
            board = sweep(board, cell[0], cell[1]);
        }
        return board;
    }

    private static List<int[]> getNeighboringCells(Cell[][] board, int x, int y) {
        List<int[]> neighbouringCells = new ArrayList<>(); // create an array of all cells that have a number in them
        if (board[x][y].hasNumbers) {
            return neighbouringCells; // if the current cell being looked at has a number in, return to sweep
        }
        if ((x + 1) < board.length && (!board[x + 1][y].isMine && !board[x + 1][y].isRevealed)) { // checking every direction
            int[] neighbouringCell = {x + 1, y};
            neighbouringCells.add(neighbouringCell);
        }
        if ((x - 1) >= 0 && (!board[x - 1][y].isMine && !board[x - 1][y].isRevealed)) {
            int[] neighbouringCell = {x - 1, y};
            neighbouringCells.add(neighbouringCell);
        }
        if ((y + 1) < board.length && (!board[x][y + 1].isMine && !board[x][y + 1].isRevealed)) {
            int[] neighbouringCell = {x, y + 1};
            neighbouringCells.add(neighbouringCell);
        }
        if ((y - 1) >= 0 && (!board[x][y - 1].isMine && !board[x][y - 1].isRevealed)) {
            int[] neighbouringCell = {x, y - 1};
            neighbouringCells.add(neighbouringCell);
        }

        if ((x - 1) >= 0 && (y - 1) >= 0 && (!board[x - 1][y - 1].isMine && !board[x - 1][y - 1].isRevealed)) {
            int[] neighbouringCell = {x - 1, y - 1};
            neighbouringCells.add(neighbouringCell);
        }
        if ((x + 1) < board.length && (y + 1) < board.length && (!board[x + 1][y + 1].isMine && !board[x + 1][y + 1].isRevealed)) {
            int[] neighbouringCell = {x + 1, y + 1};
            neighbouringCells.add(neighbouringCell);
        }
        if ((x - 1) >= 0 && (y + 1) < board.length && (!board[x - 1][y + 1].isMine && !board[x - 1][y + 1].isRevealed)) {
            int[] neighbouringCell = {x - 1, y + 1};
            neighbouringCells.add(neighbouringCell);
        }
        if ((x + 1) < board.length && (y - 1) >= 0 && (!board[x + 1][y - 1].isMine && !board[x + 1][y - 1].isRevealed)) {
            int[] neighbouringCell = {x + 1, y - 1};
            neighbouringCells.add(neighbouringCell);
        }
        return neighbouringCells;
    }

    private static boolean checkIfGameIsWon(Cell[][] board) {
        for (Cell[] cells : board) {
            for (int j = 0; j < board.length; j++) {
                if (!cells[j].isRevealed && !cells[j].isMine && !cells[j].isFlagged) {
                    return false;
                }
            }
        }
        System.out.println("Congratulations on winning the Game!!");
        return true;
    }

    public void countNeighboringMines(Cell[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (!board[i][j].isMine) {
                    int mineCount = countMinesInNeighborhood(board, i, j);
                    board[i][j].mineCounter = mineCount;
                    board[i][j].hasNumbers = (mineCount > 0);
                }
            }
        }
    }

    private int countMinesInNeighborhood(Cell[][] board, int row, int col) {
        int mineCount = 0;
        for (int q = -1; q <= 1; q++) {
            for (int w = -1; w <= 1; w++) {
                int x = row + q;
                int y = col + w;

                if (x >= 0 && x < board.length && y >= 0 && y < board[0].length && board[x][y].isMine) {
                    mineCount++;
                }
            }
        }
        return mineCount;
    }


    public static void main(String[] args) {
        Main mineSweeper = new Main();
        mineSweeper.minesweeperGame();

        // firstly - create a board
        // 2nd - random number of mines. place mines in the board
        // 3rd - display the board to the player.
        // 4ty - take the user input
        // 5th - check if user hits bomb- if they do - game is lost
        // 6th - check if user doesn't hit bomb -
        // 7th - then display the maths associated with minesweeper
        // 8th - if hit bomb display shake thing.
    }
}
