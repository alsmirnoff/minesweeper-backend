package com.example.testtask.minesweeper_backend.entity;

import java.util.UUID;

import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(nullable = false)
    private int rows;

    @Column(nullable = false)
    private int cols;

    @Column(name = "mines_count", nullable = false)
    private int minesCount;
    
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Cell[][] board;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameState state;

    @Version
    private Long version;

}
