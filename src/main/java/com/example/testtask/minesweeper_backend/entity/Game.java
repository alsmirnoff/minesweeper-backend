package com.example.testtask.minesweeper_backend.entity;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    private UUID gameId;

    private GameState state;

    private Cell[][] mines;

    public Game(UUID gameId, Cell[][] mines){
        this.gameId = gameId;
        this.state = GameState.ACTIVE;
        this.mines = mines;
    }

}
