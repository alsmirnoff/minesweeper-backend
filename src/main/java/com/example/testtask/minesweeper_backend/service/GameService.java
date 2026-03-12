package com.example.testtask.minesweeper_backend.service;

import java.util.UUID;

import com.example.testtask.minesweeper_backend.entity.Cell;
import com.example.testtask.minesweeper_backend.entity.Field;
import com.example.testtask.minesweeper_backend.entity.Game;
import com.example.testtask.minesweeper_backend.entity.GameTurnRequest;

public interface GameService {

    public Cell[][] initializeBoardGame(Field field);

    public void randomlyLocaledMines(Field field, Cell[][] matrix);

    public void locateMinesAround(Field field, Cell[][] matrix);

    public boolean mineFound(Cell[][] matrix, int ros, int col);

    public boolean alreadyWon(Cell[][] matrix);

    public void clearEmptySpots(Cell[][] matrix, int x, int y, int xMax, int yMax);

    public Game creatGame(Field field);

    public Game getGame(UUID id);

    public Game play(UUID id, GameTurnRequest request, Field field);
    
}
