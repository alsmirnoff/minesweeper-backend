package com.example.testtask.minesweeper_backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewGameRequest {

    @Min(2)
    @Max(50)
    private int width;

    @Min(2)
    @Max(50)
    private int height;

    @Min(1)
    @Max(2499) // width * height - 1
    private int mines_count;
}
