package com.WebSiteChoiGame.DoAnJ2EE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "GameGenres")
public class GameGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer genreID;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "genre")
    @JsonIgnore
    private List<Game> games;

    // Getters and setters
    public Integer getGenreID() { return genreID; }
    public void setGenreID(Integer genreID) { this.genreID = genreID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Game> getGames() { return games; }
    public void setGames(List<Game> games) { this.games = games; }
} 