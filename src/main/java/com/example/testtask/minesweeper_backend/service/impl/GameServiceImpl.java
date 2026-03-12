package com.example.testtask.minesweeper_backend.service.impl;

import java.util.Random;
import java.util.UUID;

import com.example.testtask.minesweeper_backend.entity.Cell;
import com.example.testtask.minesweeper_backend.entity.Field;
import com.example.testtask.minesweeper_backend.entity.Game;
import com.example.testtask.minesweeper_backend.entity.GameTurnRequest;
import com.example.testtask.minesweeper_backend.service.GameService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameServiceImpl implements GameService {

    private Cell matrixBoard[][];

    @Override
    public Cell[][] initializeBoardGame(Field field) {
        Cell matrix[][];
        matrix = new Cell[field.getRows()][];
        for (int i = 0; i < field.getRows(); i++) {
            for (int j = 0; j < field.getCols(); j++) {
                matrix[i][j] = new Cell();
            }
        }
        log.info("[Minesweeper Service] - A new game was initialized with rows={}, columns={}, mines={} for uuid={}",
				field.getRows(), field.getCols(), field.getMines(), field.getGameId());
        return matrix;
    }

    @Override
    public void randomlyLocaledMines(Field field, Cell[][] matrix) {
        int minesPlaced = 0;
        Random random = new Random();
        while(minesPlaced < field.getMines()) {
            int x = random.nextInt(field.getRows());
            int y = random.nextInt(field.getCols());
            if(!matrix[y][x].isMine()) {
                matrix[y][x].setMine(true);
                minesPlaced++;
            }
        }
        log.info("[Minesweeper Service] - Already placed mines for game of uuid={}", field.getGameId());
    }

    @Override
    public void locateMinesAround(Field field, Cell[][] matrix) {
        for (int i = 0; i < field.getRows(); i++) {
            for (int j = 0; j < field.getCols(); j++) {
                matrix[i][j].setMinesAround(minesNear(matrix, j, i));;
            }
        }
    }

    private int minesNear(Cell[][] matrix, int x, int y) {
        int mines = 0;
        mines += minesAt(matrix, x - 1, y - 1);
        mines += minesAt(matrix, x - 1, y);
        mines += minesAt(matrix, x - 1, y + 1);
        mines += minesAt(matrix, x, y + 1);
        mines += minesAt(matrix, x + 1, y + 1);
        mines += minesAt(matrix, x + 1, y);
        mines += minesAt(matrix, x + 1, y - 1);
        mines += minesAt(matrix, x, y - 1);
        return mines;
    }

    private int minesAt(Cell[][] matrix, int x, int y) {
        if(y > 0 && y < matrix[0].length && x >= 0 && x < matrix.length && matrix[y][x].isMine()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean mineFound(Cell[][] matrix, int row, int col) {
        return matrix[row][col].isMine();
    }

    @Override
    public boolean alreadyWon(Cell[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if(!matrix[i][j].isMine() && !matrix[i][j].isRevealed()){
                    return false;
                }
            }
        }
        log.info("[Minesweeper Service] - Already won a game");
        return true;
    }

    @Override
    public void clearEmptySpots(Cell[][] matrix, int x, int y, int xMax, int yMax) {
        if(x < 0 || x > xMax || y < 0 || y > yMax) {
            return;
        }
        if(matrix[x][y].getMinesAround() == 0 && !matrix[x][y].isRevealed()) {
            matrix[x][y].setRevealed(true);
            clearEmptySpots(matrix, x - 1, y - 1, xMax, yMax);
            clearEmptySpots(matrix, x - 1, y, xMax, yMax);
            clearEmptySpots(matrix, x - 1, y + 1, xMax, yMax);
            clearEmptySpots(matrix, x, y + 1, xMax, yMax);
            clearEmptySpots(matrix, x + 1, y + 1, xMax, yMax);
            clearEmptySpots(matrix, x + 1, y, xMax, yMax);
            clearEmptySpots(matrix, x + 1, y - 1, xMax, yMax);
            clearEmptySpots(matrix, x, y - 1, xMax, yMax);
        } else {
            return;
        }
    }

    @Override
    public Game creatGame(Field field) {
        log.info("[Minesweeper Service] Setting mines randomly for game of uuid={}", field.getGameId());
        matrixBoard = initializeBoardGame(field);
        randomlyLocaledMines(field, matrixBoard);
        locateMinesAround(field, matrixBoard);
        return new Game(field.getGameId(), matrixBoard);
    }

    @Override
    public Game getGame(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGame'");
    }

    @Override
    public Game play(UUID id, GameTurnRequest request, Field field) {
        int row = request.getRow();
        int col = request.getCol();

        matrixBoard = creatGame(field).getMines();

        
    }

    

}
