package com.example.testtask.minesweeper_backend;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.testtask.minesweeper_backend.entity.Cell;
import com.example.testtask.minesweeper_backend.entity.GameState;
import com.example.testtask.minesweeper_backend.mapper.GameMapper;

public class GameMapperTest {

    private final GameMapper mapper = new GameMapper();
    private Cell[][] board = new Cell[3][3];

    @BeforeEach
    public void setUp() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = new Cell();
            }
        }   
    }

    @Test
    public void testMapperUnrevealed() {
        String[][] field = mapper.convertBoardToField(board, GameState.ACTIVE);

        assertEquals(" ", field[0][0]);
    }

    @Test
    public void testMapperMineLost() {
        board[1][1].setMine(true);
        board[1][1].setRevealed(true);

        String[][] field = mapper.convertBoardToField(board, GameState.LOST);

        assertEquals("X", field[1][1]);
    }

    @Test
    public void testMapperMineWin() {
        board[1][1].setMine(true);
        board[1][1].setRevealed(true);
        String[][] field = mapper.convertBoardToField(board, GameState.WON);

        assertEquals("M", field[1][1]);
    }

    @Test
    public void testMapperRevealedNumber() {
        board[1][1].setMine(true);
        board[1][1].setRevealed(false);
        board[0][0].setMinesAround(1);
        board[0][0].setRevealed(true);

        String[][] field = mapper.convertBoardToField(board, GameState.ACTIVE);

        assertEquals("1", field[0][0]);
    }

}
