package com.example.testtask.minesweeper_backend.mapper;

import org.springframework.stereotype.Component;

import com.example.testtask.minesweeper_backend.dto.GameInfoResponse;
import com.example.testtask.minesweeper_backend.entity.Cell;
import com.example.testtask.minesweeper_backend.entity.Game;
import com.example.testtask.minesweeper_backend.entity.GameState;

@Component
public class GameMapper {

    public String[][] convertBoardToField(Cell[][] board, GameState state) {
        if(board == null || board.length == 0) {
            return new String[0][0];
        }

        int rows = board.length;
        int cols = board[0].length;
        String[][] field = new String[rows][cols];

        boolean isLost = (state == GameState.LOST);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                field[r][c] = convertCellToString(board[r][c], isLost);
            }
        }
        return field;
    }

    private String convertCellToString(Cell cell, boolean isLost) {
        if(!cell.isRevealed()) {
            return " ";
        } else if(cell.isMine()) {
            return isLost ? "X" : "M";
        } else {
            return String.valueOf(cell.getMinesAround());
        }
    }

    public GameInfoResponse toResponse(Game game) {
        return new GameInfoResponse(
                    game.getId(), 
                    game.getCols(), 
                    game.getRows(), 
                    game.getMinesCount(),
                    game.getState() != GameState.ACTIVE,
                    convertBoardToField(game.getBoard(), game.getState())
        );
        
            // .builder()
            // .game_id(game.getId())
            // .width(game.getCols())
            // .height(game.getRows())
            // .mines_count(game.getMinesCount())
            // .completed(game.getState() != GameState.ACTIVE)
            // .field(convertBoardToField(game.getBoard(), game.getState()))
            // .build();
    }
}
