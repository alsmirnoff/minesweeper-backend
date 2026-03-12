package com.example.testtask.minesweeper_backend.service;

import java.util.UUID;

import com.example.testtask.minesweeper_backend.entity.Game;

public interface GameService {

    public Game createGame(int width, int height, int minesCount);
    public Game makeMove(UUID id, int rows, int cols);   
    
}
