package com.example.testtask.minesweeper_backend.advice;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.testtask.minesweeper_backend.dto.ErrorResponse;
import com.example.testtask.minesweeper_backend.exception.GameException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GameException.class)
    public ResponseEntity<ErrorResponse> handleClientErrors(GameException ex) {
        log.warn("[Minesweeper Exception] - Game error: {}", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("[Minesweeper Exception] - Validation failed: {}", message);

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(message));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedError(Exception ex) {
        log.error("[Minesweeper Exception] - Unexpected error occurred: {}", ex);

        return ResponseEntity
                .internalServerError()
                .body(new ErrorResponse("произошла непредвиденная ошибка"));
    }
}
