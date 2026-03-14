package com.example.testtask.minesweeper_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record NewGameRequest(
    @Min(2) @Max(50) int width,
    @Min(2) @Max(50) int height,
    @Min(1) @Max(2499) int mines_count  // width * height - 1
) {}
