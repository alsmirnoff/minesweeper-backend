package com.example.testtask.minesweeper_backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.testtask.minesweeper_backend.dao.InMemoryGameRepository;
import com.example.testtask.minesweeper_backend.entity.Game;
import com.example.testtask.minesweeper_backend.entity.GameState;
import com.example.testtask.minesweeper_backend.service.impl.GameServiceImpl;

public class GameServiceImplTest {

    // InMemoryGameRepository repository = new InMemoryGameRepository();

    // GameServiceImpl service = new GameServiceImpl(repository);

    // @Test
    // public void testCreateGame() {
    //     Game game = service.createGame(5, 4, 4);

    //     assertNotNull(game);
    //     assertEquals(5, game.getRows());
    //     assertEquals(4, game.getCols());
    //     assertNotNull(game.getBoard());
    // }

    // @Test
    // public void testRevealMine() {
    //     Game game = service.createGame(1, 1, 1);
    //     Game result = service.makeMove(game.getId(), 0, 0);
    //     assertEquals(GameState.LOST, result.getState());
    // }

    // @Test
    // public void testGamePersistence() {
    //     Game game = service.createGame(5, 5, 5);
    //     assertNotNull(game.getId());

    //     Game result = service.makeMove(game.getId(), 0, 0);
    //     assertEquals(game.getId(), result.getId());
    //     assertTrue(result.getBoard()[0][0].isRevealed());
    // }

    // @Test
    // public void testBoundaryCellsCanBeRevealed() {
    //     Game game = service.createGame(3, 3, 1);
    //     service.makeMove(game.getId(), 0, 0);
    //     Game result = repository.findById(game.getId());
    //     assertTrue(result.getBoard()[0][0].isRevealed());
    // }

    // @Test
    // public void testWinCondition() {
    //     Game game = service.createGame(2, 2, 1);
    //     service.makeMove(game.getId(), 0, 0);
    //     service.makeMove(game.getId(), 0, 1);

    //     Game result = repository.findById(game.getId());
    //     assertEquals(GameState.WON, result.getState());
    // }

}
