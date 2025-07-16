package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "FavoriteGames")
public class FavoriteGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer favoriteID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "gameID", nullable = false)
    private Game game;

    private LocalDateTime createdAt;

    // Getters and setters
    public Integer getFavoriteID() { return favoriteID; }
    public void setFavoriteID(Integer favoriteID) { this.favoriteID = favoriteID; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 