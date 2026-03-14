package com.example.testtask.minesweeper_backend.service;

import com.example.testtask.minesweeper_backend.entity.Cell;

public interface MinesPlacer {
    void placeMines(Cell[][] board, int rows, int cols, int minesCount);
}
