package com.example.testtask.minesweeper_backend.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameTurnRequest {

    @NotNull
    private UUID game_id;

    @NotNull
    private Integer col;

    @NotNull
    private Integer row;
}
