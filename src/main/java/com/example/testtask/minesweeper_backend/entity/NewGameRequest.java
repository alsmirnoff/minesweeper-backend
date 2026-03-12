package com.example.testtask.minesweeper_backend.entity;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewGameRequest {
    
    private int width;

    private int height;

    private int minesCount;
}
