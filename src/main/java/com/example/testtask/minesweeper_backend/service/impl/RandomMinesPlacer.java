package com.example.testtask.minesweeper_backend.service.impl;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.example.testtask.minesweeper_backend.entity.Cell;
import com.example.testtask.minesweeper_backend.service.MinesPlacer;

@Component
public class RandomMinesPlacer implements MinesPlacer{

    private final Random random = new Random();

    @Override
    public void placeMines(Cell[][] board, int rows, int cols, int minesCount) {
        int minesPlaced = 0;
        while(minesPlaced < minesCount) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            if(!board[r][c].isMine()) {
                board[r][c].setMine(true);
                minesPlaced++;
            }
        }
    }
}
