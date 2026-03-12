package com.example.testtask.minesweeper_backend.service.impl;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.testtask.minesweeper_backend.dao.InMemoryGameRepository;
import com.example.testtask.minesweeper_backend.entity.Cell;
import com.example.testtask.minesweeper_backend.entity.Game;
import com.example.testtask.minesweeper_backend.entity.GameState;
import com.example.testtask.minesweeper_backend.service.GameService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    private final InMemoryGameRepository repository;

    public GameServiceImpl(InMemoryGameRepository repository){
        this.repository = repository;
    }

    @Override
    public Game createGame(int width, int height, int minesCount) {
        Game game = new Game();
        game.setId(UUID.randomUUID());
        game.setRows(width);
        game.setCols(height);
        game.setMinesCount(minesCount);
        game.setState(GameState.ACTIVE);

        Cell board[][] = new Cell[game.getRows()][game.getCols()];
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                board[i][j] = new Cell();
            }
        }
        game.setBoard(board);
        
        placeMinesRandomly(game);
        calculateMinesAround(game);

        repository.save(game);

        log.info("[Minesweeper Service] - Creating a game of uuid={}", game.getId());
        return game;
    }

    @Override
    public Game makeMove(UUID id, int row, int col) {
        Game game = repository.findById(id);

        if(game == null){
            log.error("[Minesweeper Service] Game with uuid={} not found", id);
            throw new RuntimeException("Game not found");
        }

        if(game.getState() != GameState.ACTIVE) {
            log.error("[Minesweeper Service] Game with uuid={} alredy end", id);
            throw new RuntimeException("Game alredy end");
        }

        processTurnLogic(game, row, col);

        repository.save(game);
        log.info("[Minesweeper Service] Move x={}, y={} made on game uuid={}", row, col, id);
        return game;
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
        mines += minesAt(board, col - 1, row - 1);
        mines += minesAt(board, col - 1, row);
        mines += minesAt(board, col - 1, row + 1);
        mines += minesAt(board, col, row + 1);
        mines += minesAt(board, col + 1, row + 1);
        mines += minesAt(board, col + 1, row);
        mines += minesAt(board, col + 1, row - 1);
        mines += minesAt(board, col, row - 1);
        return mines;
    }


    private int minesAt(Cell[][] board, int row, int col) {
        if(row >= 0 && row < board[0].length && col >= 0 && col < board.length && board[col][row].isMine()) {
            return 1;
        } else {
            return 0;
        }
    }

    private void revealArea(Cell[][] board, int row, int col) {
        if(col < 0 || col >= board.length || row < 0 || row >= board[0].length) return;

        Cell cell = board[col][row];

        if(cell.isRevealed()) return;

        cell.setRevealed(true);

        if(cell.getMinesAround() == 0) {
            revealArea(board, col - 1, row - 1);
            revealArea(board, col - 1, row);
            revealArea(board, col - 1, row + 1);
            revealArea(board, col, row + 1);
            revealArea(board, col + 1, row + 1);
            revealArea(board, col + 1, row);
            revealArea(board, col + 1, row - 1);
            revealArea(board, col, row - 1);
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
                if(cell.isMine()) {
                    cell.setRevealed(true);
                }
            }
        }
    }

}
