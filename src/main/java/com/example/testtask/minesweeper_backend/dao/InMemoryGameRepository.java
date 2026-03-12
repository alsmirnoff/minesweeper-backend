package com.example.testtask.minesweeper_backend.dao;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.testtask.minesweeper_backend.entity.Game;

@Component
public class InMemoryGameRepository {
    private final Map<UUID, Game> storage = new ConcurrentHashMap<>();

    public void save(Game game) {
        storage.put(game.getId(), game);
    }

    public Game findById(UUID id) {
        return storage.get(id);
    }
    
}
