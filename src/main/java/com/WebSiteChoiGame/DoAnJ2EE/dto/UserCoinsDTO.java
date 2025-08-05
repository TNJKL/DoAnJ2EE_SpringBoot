package com.WebSiteChoiGame.DoAnJ2EE.dto;

import java.time.LocalDateTime;

public class UserCoinsDTO {
    private Integer userID;
    private String username;
    private Integer coinAmount;
    private LocalDateTime updatedAt;

    // Constructors
    public UserCoinsDTO() {}

    public UserCoinsDTO(Integer userID, String username, Integer coinAmount, LocalDateTime updatedAt) {
        this.userID = userID;
        this.username = username;
        this.coinAmount = coinAmount;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getCoinAmount() { return coinAmount; }
    public void setCoinAmount(Integer coinAmount) { this.coinAmount = coinAmount; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 