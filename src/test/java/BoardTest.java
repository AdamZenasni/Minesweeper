import org.example.Cell;
import org.example.Main;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void testCreateBoard() {
        Main minesweeper = new Main();
        Cell[][] board = minesweeper.createBoard(10);

        assertEquals(10, board.length, "expected to create a board with ten rows");
        assertEquals(10, board[0].length, "expected to create a board with ten columns");
    }

    @Test
    public void testPlaceBombs() {
        Main minesweeper = new Main();
        Cell[][] board = minesweeper.createBoard(10);
        Main.placeBombs(10, 10, board, 0, 0);

        int bombCount = 0;
        for (Cell[] row : board) {
            for (Cell cell : row) {
                if (cell.isMine) {
                    bombCount++;
                }
            }
        }

        assertEquals(10, bombCount, "excpected to have ten bombs");
    }


    @Test
    public void testRevealCoordinates() {
        Main minesweeper = new Main();
        Cell[][] board = minesweeper.createBoard(3);

        board[0][0].isMine = true;
        board[0][1].isMine = true;
        board[1][0].isMine = true;

        Main.sweep(board, 2, 2);

        assertTrue(board[2][2].isRevealed, "expect the entered co-ordinates of 2,2 to be revealed");
        assertTrue(board[2][1].isRevealed, "expect the neigbouring cells of 2,2 (2,1) to be revealed as a result of sweeping");
        assertTrue(board[1][2].isRevealed, "expect the neigbouring cells of 2,2 (1,2) to be revealed as a result of sweeping");
        assertFalse(board[0][0].isRevealed, "expect a far away cell 0,0 to not be revealed");
    }

}
