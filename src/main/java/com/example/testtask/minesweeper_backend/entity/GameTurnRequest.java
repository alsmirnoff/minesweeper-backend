package com.example.testtask.minesweeper_backend.entity;

import lombok.Data;

@Data
public class GameTurnRequest {
    
    private int row;

    private int col;
}
