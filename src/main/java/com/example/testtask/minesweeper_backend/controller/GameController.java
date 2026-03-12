package com.example.testtask.minesweeper_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.testtask.minesweeper_backend.dto.GameInfoResponse;
import com.example.testtask.minesweeper_backend.dto.GameTurnRequest;
import com.example.testtask.minesweeper_backend.dto.NewGameRequest;
import com.example.testtask.minesweeper_backend.entity.Cell;
import com.example.testtask.minesweeper_backend.entity.Game;
import com.example.testtask.minesweeper_backend.entity.GameState;
import com.example.testtask.minesweeper_backend.service.impl.GameServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameServiceImpl gameService;

    @PostMapping("/new")
    public ResponseEntity<GameInfoResponse> createGame(@RequestBody @Valid NewGameRequest request) {
        log.info("[Minesweeper Controller] - Creating game {}x{}, mines={}", request.getHeight(), request.getWidth(), request.getMines_count());

        Game game = gameService.createGame(request.getWidth(), request.getHeight(), request.getMines_count());
        GameInfoResponse response = buildResponse(game);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/turn")
    public ResponseEntity<GameInfoResponse> makeTurn(@RequestBody @Valid GameTurnRequest request) {
        log.info("[Minesweeper Controller] - Turn: game={}, row={}, col={}", request.getGame_id(), request.getRow(), request.getCol());

        Game game = gameService.makeMove(request.getGame_id(), request.getRow(), request.getCol());

        GameInfoResponse response = buildResponse(game);
        return ResponseEntity.ok(response);
    }

    private GameInfoResponse buildResponse(Game game) {
        return new GameInfoResponse(
            game.getId(),
            game.getRows(),
            game.getCols(),
            game.getMinesCount(),
            game.getState() != GameState.ACTIVE,
            convertBoardToResponse(game.getBoard(), game.getState())
        );
    }

    private String[][] convertBoardToResponse(Cell[][] board, GameState state) {
        int rows = board.length;
        int cols = board[0].length;
        String[][] result = new String[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = board[r][c];
                if(!cell.isRevealed()) result[r][c] = " ";
                else if(cell.isMine()) result[r][c] = "M";
                else result[r][c] = String.valueOf(cell.getMinesAround());
            }
        }

        return result;
    }
}
