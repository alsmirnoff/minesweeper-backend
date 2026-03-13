package com.example.testtask.minesweeper_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.testtask.minesweeper_backend.dto.GameInfoResponse;
import com.example.testtask.minesweeper_backend.dto.GameTurnRequest;
import com.example.testtask.minesweeper_backend.dto.NewGameRequest;
import com.example.testtask.minesweeper_backend.entity.Game;
import com.example.testtask.minesweeper_backend.mapper.GameMapper;
import com.example.testtask.minesweeper_backend.service.impl.GameServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameServiceImpl gameService;
    private final GameMapper gameMapper;

    @PostMapping("/new")
    public ResponseEntity<GameInfoResponse> createGame(@RequestBody @Valid NewGameRequest request) {
        log.info("[Minesweeper Controller] - Creating game {}x{}, mines={}", request.getHeight(), request.getWidth(), request.getMines_count());

        Game game = gameService.createGame(
            request.getHeight(), 
            request.getWidth(), 
            request.getMines_count()
        );
        
        return ResponseEntity.ok(gameMapper.toResponse(game));
    }

    @PostMapping("/turn")
    public ResponseEntity<GameInfoResponse> makeTurn(@RequestBody @Valid GameTurnRequest request) {
        log.info("[Minesweeper Controller] - Turn: game={}, row={}, col={}", request.getGame_id(), request.getRow(), request.getCol());

        Game game = gameService.makeMove(
            request.getGame_id(), 
            request.getRow(), 
            request.getCol()
        );

        return ResponseEntity.ok(gameMapper.toResponse(game));
    }

}
