package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "GameRatings")
public class GameRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ratingID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "gameID", nullable = false)
    private Game game;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 500)
    private String comment;

    private LocalDateTime ratedAt;

    // Getters and setters
    public Integer getRatingID() { return ratingID; }
    public void setRatingID(Integer ratingID) { this.ratingID = ratingID; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getRatedAt() { return ratedAt; }
    public void setRatedAt(LocalDateTime ratedAt) { this.ratedAt = ratedAt; }
} 