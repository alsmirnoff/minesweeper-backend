package com.example.testtask.minesweeper_backend.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameInfoResponse {

    private UUID game_id;
    private int width;
    private int height;
    private int mines_count;
    private boolean completed;
    private String[][] field;

}
