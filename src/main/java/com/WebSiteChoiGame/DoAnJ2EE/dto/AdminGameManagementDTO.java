package com.WebSiteChoiGame.DoAnJ2EE.dto;

import com.WebSiteChoiGame.DoAnJ2EE.entity.Game;
import java.time.LocalDateTime;

public class AdminGameManagementDTO {
    private Integer gameID;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String gameUrl;
    private String gameType;
    private Boolean isVisible;
    private Boolean isApproved;
    private LocalDateTime createdAt;
    
    // Chỉ lấy thông tin cần thiết thay vì toàn bộ object
    private Integer genreID;
    private String genreName;
    private Integer createdByID;
    private String createdByUsername;

    public AdminGameManagementDTO(Game game) {
        this.gameID = game.getGameID();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.thumbnailUrl = game.getThumbnailUrl();
        this.gameUrl = game.getGameUrl();
        this.gameType = game.getGameType();
        this.isVisible = game.getIsVisible();
        this.isApproved = game.getIsApproved();
        this.createdAt = game.getCreatedAt();
        
        // Lấy thông tin genre
        if (game.getGenre() != null) {
            this.genreID = game.getGenre().getGenreID();
            this.genreName = game.getGenre().getName();
        }
        
        // Lấy thông tin user tạo
        if (game.getCreatedBy() != null) {
            this.createdByID = game.getCreatedBy().getUserID();
            this.createdByUsername = game.getCreatedBy().getUsername();
        }
    }

    // Getters và Setters
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
    
    public Boolean getIsVisible() { return isVisible; }
    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
    
    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Integer getGenreID() { return genreID; }
    public void setGenreID(Integer genreID) { this.genreID = genreID; }
    
    public String getGenreName() { return genreName; }
    public void setGenreName(String genreName) { this.genreName = genreName; }
    
    public Integer getCreatedByID() { return createdByID; }
    public void setCreatedByID(Integer createdByID) { this.createdByID = createdByID; }
    
    public String getCreatedByUsername() { return createdByUsername; }
    public void setCreatedByUsername(String createdByUsername) { this.createdByUsername = createdByUsername; }
} 