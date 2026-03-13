package com.example.testtask.minesweeper_backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.testtask.minesweeper_backend.entity.Game;
import com.example.testtask.minesweeper_backend.service.impl.GameServiceImpl;

@SpringBootTest
@Testcontainers
public class GameServiceImplIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");

    @DynamicPropertySource
    static void configurePropersties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private GameServiceImpl service;

    @Test
    public void testCreateAndMakeMoveWithDB() {
        Game game = service.createGame(5, 5, 3);
        assertNotNull(game);

        Game result = service.makeMove(game.getId(), 0, 0);

        assertEquals(game.getId(), result.getId());
        assertTrue(result.getBoard()[0][0].isRevealed());
    }
}
