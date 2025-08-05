package com.WebSiteChoiGame.DoAnJ2EE.dto;

import com.WebSiteChoiGame.DoAnJ2EE.entity.Game;
import com.WebSiteChoiGame.DoAnJ2EE.entity.GameGenre;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import java.time.LocalDateTime;

public class AdminGameDTO {
    private Integer gameID;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String gameUrl;
    private String gameType;
    private Boolean isVisible;
    private Boolean isApproved;
    private GameGenre genre;
    private User createdBy;
    private LocalDateTime createdAt;

    // Constructor từ Game entity
    public AdminGameDTO(Game game) {
        this.gameID = game.getGameID();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.thumbnailUrl = game.getThumbnailUrl();
        this.gameUrl = game.getGameUrl();
        this.gameType = game.getGameType();
        this.isVisible = game.getIsVisible();
        this.isApproved = game.getIsApproved();
        this.genre = game.getGenre();
        this.createdBy = game.getCreatedBy();
        this.createdAt = game.getCreatedAt();
    }

    // Getters và Setters
    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public GameGenre getGenre() {
        return genre;
    }

    public void setGenre(GameGenre genre) {
        this.genre = genre;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 