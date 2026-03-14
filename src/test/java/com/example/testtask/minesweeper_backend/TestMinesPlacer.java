package com.example.testtask.minesweeper_backend;

import java.util.HashSet;
import java.util.Set;

import com.example.testtask.minesweeper_backend.entity.Cell;
import com.example.testtask.minesweeper_backend.service.MinesPlacer;

public class TestMinesPlacer implements MinesPlacer{

    private final Set<String> minesPositions;

    public TestMinesPlacer(int... coords){
        this.minesPositions = new HashSet<>();
        for (int i = 0; i < coords.length; i += 2) {
            minesPositions.add(coords[i] + ":" + coords[i+1]);
        }
    }

    @Override
    public void placeMines(Cell[][] board, int rows, int cols, int minesCount) {
        for (String pos : minesPositions) {
            String[] parts = pos.split(":");
            int r = Integer.parseInt(parts[0]);
            int c = Integer.parseInt(parts[1]);
            board[r][c].setMine(true);
        }
    }


}
