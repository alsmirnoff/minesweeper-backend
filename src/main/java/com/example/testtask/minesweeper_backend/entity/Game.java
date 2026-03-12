package com.example.testtask.minesweeper_backend.entity;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    private UUID id;
    private int rows;
    private int cols;
    private int minesCount;
    private Cell[][] board;
    private GameState state;

}
