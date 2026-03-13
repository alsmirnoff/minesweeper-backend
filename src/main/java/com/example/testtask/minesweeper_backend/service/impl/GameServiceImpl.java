package com.example.testtask.minesweeper_backend.service.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.testtask.minesweeper_backend.dao.GameRepository;
import com.example.testtask.minesweeper_backend.dao.InMemoryGameRepository;
import com.example.testtask.minesweeper_backend.entity.Cell;
import com.example.testtask.minesweeper_backend.entity.Game;
import com.example.testtask.minesweeper_backend.entity.GameState;
import com.example.testtask.minesweeper_backend.service.GameService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * Система координат:
 * - Доска: Cell[rows][cols], доступ board[row][col]
 * - row: 0..height-1 (вертикаль, сверху вниз, y)
 * - col: 0..width-1 (горизонталь, слева направо, x)
 * - Все методы принимают параметры в порядке (row, col)
 */

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    // private final InMemoryGameRepository repository;
    private final GameRepository repository;

    public GameServiceImpl(GameRepository repository){
        this.repository = repository;
    }

    @Override
    public Game createGame(int rows, int cols, int minesCount) {
        Game game = Game.builder()
            .rows(rows)
            .cols(cols)
            .minesCount(minesCount)
            .state(GameState.ACTIVE)
            .board(new Cell[rows][cols])
            .build();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                game.getBoard()[i][j] = new Cell();
            }
        }

        placeMinesRandomly(game);
        calculateMinesAround(game);

        Game saved = repository.save(game);

        log.info("[Minesweeper Service] - Creating a game of uuid={}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Game makeMove(UUID id, int row, int col) {
        Game game = repository.findById(id)
            .orElseThrow(() -> {
                log.error("[Minesweeper Service] Game with uuid={} not found", id);
                throw new RuntimeException("Game not found");
            });

        if(game.getState() != GameState.ACTIVE) {
            log.error("[Minesweeper Service] Game with uuid={} alredy end", id);
            throw new RuntimeException("Game alredy end");
        }

        processTurnLogic(game, row, col);

        Game updated = repository.save(game);

        log.info("[Minesweeper Service] Make move row={}, col={} made on game uuid={}", row, col, id);
        return updated;
    }

    private void placeMinesRandomly(Game game) {
        int minesPlaced = 0;
        Cell[][] board = game.getBoard();
        Random random = new Random();
        while(minesPlaced < game.getMinesCount()) {
            int r = random.nextInt(game.getRows());
            int c = random.nextInt(game.getCols());
            if(!board[r][c].isMine()) {
                board[r][c].setMine(true);
                minesPlaced++;
            }
        }
        log.info("[Minesweeper Service] - Placed random mines for game of uuid={}", game.getId());
    }

    private void calculateMinesAround(Game game) {
        Cell[][] board = game.getBoard();
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                board[i][j].setMinesAround(minesNear(board, i, j));
            }
        }
        log.info("[Minesweeper Service] - Calculating surroundings mines for game of uuid={}", game.getId());
    }

    private int minesNear(Cell[][] board, int row, int col) {
        int mines = 0;
        mines += minesAt(board, row - 1, col - 1);
        mines += minesAt(board, row, col - 1);
        mines += minesAt(board, row + 1, col - 1);
        mines += minesAt(board, row + 1, col);
        mines += minesAt(board, row + 1, col + 1);
        mines += minesAt(board, row, col + 1);
        mines += minesAt(board, row - 1, col + 1);
        mines += minesAt(board, row - 1, col);
        return mines;
    }


    private int minesAt(Cell[][] board, int row, int col) {
        if (row >= 0 && row < board.length && 
            col >= 0 && col < board[0].length && 
            board[row][col].isMine()) {
                return 1;
            }
        return 0;
    }

    private void revealArea(Cell[][] board, int row, int col) {
        if (row < 0 || row >= board.length || 
            col < 0 || col >= board[0].length) return;

        Cell cell = board[row][col];

        if(cell.isRevealed()) return;

        cell.setRevealed(true);

        if(cell.getMinesAround() == 0) {
            revealArea(board, row - 1, col - 1);
            revealArea(board, row, col - 1);
            revealArea(board, row + 1, col - 1);
            revealArea(board, row + 1, col);
            revealArea(board, row + 1, col + 1);
            revealArea(board, row, col + 1);
            revealArea(board, row - 1, col + 1);
            revealArea(board, row - 1, col);
        }
    }

    private void processTurnLogic(Game game, int row, int col){
        Cell[][] board = game.getBoard();

        if(row < 0 || row >= game.getRows() || col < 0 || col >= game.getCols()) {
            log.warn("[Minesweeper Service] - Illegal coordinates row={}, col={}", row, col);
            throw new IllegalArgumentException("Illegal coordinates");
        }

        Cell clicked = board[row][col];

        if(clicked.isRevealed()) {
            log.warn("[Minesweeper Service] - Cell row={}, col={} already opened", row, col);
            throw new IllegalStateException("Cell already opened");
        }

        if(clicked.isMine()) {
            clicked.setRevealed(true);
            game.setState(GameState.LOST);
            revealAllMines(game.getBoard());
            return;
        }

        if(clicked.getMinesAround() == 0) {
            revealArea(board, row, col);
        } else {
            clicked.setRevealed(true);
        }

        if(checkWinCondition(game)) {
            game.setState(GameState.WON);
            revealAllMines(game.getBoard());
        }
    }

    private boolean checkWinCondition(Game game) {
        Cell[][] board = game.getBoard();
        for (int r = 0; r < game.getRows(); r++) {
            for (int c = 0; c < game.getCols(); c++) {
                Cell cell = board[r][c];
                if(!cell.isMine() && !cell.isRevealed()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void revealAllMines(Cell[][] board) {
        for (Cell[] row : board) {
            for (Cell cell : row) {
                cell.setRevealed(true);
                if(cell.isMine()) {
                    cell.setRevealed(true);
                }
            }
        }
    }

}
