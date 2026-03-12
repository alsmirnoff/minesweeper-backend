package com.example.testtask.minesweeper_backend.entity;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cell {
    private boolean revealed;
    private int minesAround;
    private boolean mine;
}
