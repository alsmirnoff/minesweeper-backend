package com.example.testtask.minesweeper_backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.testtask.minesweeper_backend.dto.ErrorResponse;
import com.example.testtask.minesweeper_backend.dto.GameInfoResponse;
import com.example.testtask.minesweeper_backend.dto.GameTurnRequest;
import com.example.testtask.minesweeper_backend.dto.NewGameRequest;
import com.example.testtask.minesweeper_backend.service.MinesPlacer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(GameApiIntegrationTest.TestConfig.class)
@Testcontainers
public class GameApiIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");

    @DynamicPropertySource
    static void configurePropersties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    private GameInfoResponse createGame(int width, int height, int minesCount) {
        NewGameRequest request = new NewGameRequest(width, height, minesCount);

        ResponseEntity<GameInfoResponse> response = restTemplate.postForEntity(
            "/api/new", request, GameInfoResponse.class);
        
        return response.getBody();
        
    }

    @Test
    public void testCreateGame_Returns200() {
        NewGameRequest request = new NewGameRequest(2, 2, 1);
        
        ResponseEntity<GameInfoResponse> response = restTemplate.postForEntity(
            "/api/new", request, GameInfoResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getGame_id());
        assertEquals(2, response.getBody().getWidth());

    }

    @Test
    public void testCreateGame_AndMakeMove_Returns200() {
        GameInfoResponse game = createGame(2, 2, 1);
        assertNotNull(game.getGame_id());

        GameTurnRequest firstMove = new GameTurnRequest(game.getGame_id(), 0, 0);
        ResponseEntity<GameInfoResponse> moveResponse = restTemplate.postForEntity(
            "/api/turn", firstMove, GameInfoResponse.class);

        assertEquals(HttpStatus.OK, moveResponse.getStatusCode());
        assertNotNull(moveResponse.getBody());
        assertNotNull(moveResponse.getBody().getGame_id());

    }

    @Test
    public void testMakeMove_OnRevealed_Returns400() {
        GameInfoResponse game = createGame(2, 2, 1);

        GameTurnRequest firstMove = new GameTurnRequest(game.getGame_id(), 0, 0);
        ResponseEntity<GameInfoResponse> moveResponse = restTemplate.postForEntity(
            "/api/turn", firstMove, GameInfoResponse.class);

        ResponseEntity<ErrorResponse> errorResponse = restTemplate.postForEntity(
            "/api/turn", firstMove, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        assertNotNull(errorResponse.getBody());
        assertNotNull(errorResponse.getBody().getError());

    }

    @Test
    public void testMakeMove_OnMine_ReturnsLostState() {
        GameInfoResponse game = createGame(2, 2, 1);

        GameTurnRequest firstMove = new GameTurnRequest(game.getGame_id(), 1, 1);
        ResponseEntity<GameInfoResponse> moveResponse = restTemplate.postForEntity(
            "/api/turn", firstMove, GameInfoResponse.class);

        assertEquals(HttpStatus.OK, moveResponse.getStatusCode());
        assertNotNull(moveResponse.getBody().isCompleted());

        assertEquals("X", moveResponse.getBody().getField()[1][1]);

    }

    @Test
    public void testMakeMoves_ReturnsWinState() {
        GameInfoResponse game = createGame(2, 2, 1);

        GameTurnRequest firstMove = new GameTurnRequest(game.getGame_id(), 0, 0);
        ResponseEntity<GameInfoResponse> firstMoveResponse = restTemplate.postForEntity(
            "/api/turn", firstMove, GameInfoResponse.class);

        GameTurnRequest secondMove = new GameTurnRequest(game.getGame_id(), 0, 1);
        ResponseEntity<GameInfoResponse> secondMoveResponse = restTemplate.postForEntity(
            "/api/turn", secondMove, GameInfoResponse.class);

        GameTurnRequest thirdMove = new GameTurnRequest(game.getGame_id(), 1, 0);
        ResponseEntity<GameInfoResponse> thirdMoveResponse = restTemplate.postForEntity(
            "/api/turn", thirdMove, GameInfoResponse.class);

        assertEquals(HttpStatus.OK, thirdMoveResponse.getStatusCode());
        assertNotNull(thirdMoveResponse.getBody().isCompleted());

        assertEquals("M", thirdMoveResponse.getBody().getField()[1][1]);

    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public MinesPlacer minesPlacer() {
            return new TestMinesPlacer(1,1);
        }
    }
}
