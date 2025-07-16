package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ScoreBoard")
public class ScoreBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scoreID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "gameID", nullable = false)
    private Game game;

    @Column(nullable = false)
    private Integer highScore;

    // Getters and setters
    public Integer getScoreID() { return scoreID; }
    public void setScoreID(Integer scoreID) { this.scoreID = scoreID; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public Integer getHighScore() { return highScore; }
    public void setHighScore(Integer highScore) { this.highScore = highScore; }
} 