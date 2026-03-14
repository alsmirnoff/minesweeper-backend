package com.example.testtask.minesweeper_backend.dto;

import java.util.UUID;

public record GameInfoResponse(
    UUID game_id,
    int width,
    int height,
    int mines_count,
    boolean completed,
    String[][] field
) {}
