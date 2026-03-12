package com.example.testtask.minesweeper_backend.entity;

import java.util.UUID;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Field {

    private UUID gameId;

    private int cols;

    private int rows;

    private int mines;

}