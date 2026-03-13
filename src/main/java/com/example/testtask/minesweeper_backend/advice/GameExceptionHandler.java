package com.example.testtask.minesweeper_backend.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.testtask.minesweeper_backend.dto.ErrorResponse;
import com.example.testtask.minesweeper_backend.exception.GameException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GameExceptionHandler {

    @ExceptionHandler(GameException.class)
    public ResponseEntity<ErrorResponse> handleClientErrors(GameException ex) {
        log.warn("Client error: {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }

}
