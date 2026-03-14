package com.example.testtask.minesweeper_backend.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record GameTurnRequest(
    @NotNull UUID game_id,
    @NotNull Integer col,
    @NotNull Integer row
) {}
