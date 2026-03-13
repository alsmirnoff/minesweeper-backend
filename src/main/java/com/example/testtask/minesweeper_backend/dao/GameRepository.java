package com.example.testtask.minesweeper_backend.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testtask.minesweeper_backend.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID>{

}
