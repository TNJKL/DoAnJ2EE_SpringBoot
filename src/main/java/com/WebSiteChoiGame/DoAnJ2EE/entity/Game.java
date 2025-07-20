package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gameID;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    private String thumbnailUrl;

    @Column(length = 500)
    private String gameUrl; // URL đến game thực tế

    @Column(length = 50)
    private String gameType; // Loại game: "snake", "puzzle", "racing", etc.

    @ManyToOne
    @JoinColumn(name = "genreID", nullable = false)
    private GameGenre genre;

    @ManyToOne
    @JoinColumn(name = "createdBy", nullable = false)
    private User createdBy;

    private Boolean isApproved;
    private Boolean isVisible;
    private LocalDateTime createdAt;

    // Getters and setters
    public Integer getGameID() { return gameID; }
    public void setGameID(Integer gameID) { this.gameID = gameID; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getGameUrl() { return gameUrl; }
    public void setGameUrl(String gameUrl) { this.gameUrl = gameUrl; }
    public String getGameType() { return gameType; }
    public void setGameType(String gameType) { this.gameType = gameType; }
    public GameGenre getGenre() { return genre; }
    public void setGenre(GameGenre genre) { this.genre = genre; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }
    public Boolean getIsVisible() { return isVisible; }
    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 