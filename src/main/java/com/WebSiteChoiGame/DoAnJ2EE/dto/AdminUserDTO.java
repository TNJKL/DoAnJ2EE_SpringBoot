package com.WebSiteChoiGame.DoAnJ2EE.dto;

import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import java.time.LocalDateTime;

public class AdminUserDTO {
    private Integer userID;
    private String username;
    private String email;
    private String passwordHash;
    private String googleID;
    private String roleName;
    private Integer coinAmount;
    private LocalDateTime createdAt;

    // Constructor từ User entity
    public AdminUserDTO(User user) {
        this.userID = user.getUserID();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.passwordHash = user.getPasswordHash();
        this.googleID = user.getGoogleID();
        this.roleName = user.getRole() != null ? user.getRole().getRoleName() : null;
        this.coinAmount = user.getUserCoins() != null ? user.getUserCoins().getCoinAmount() : 0;
        this.createdAt = user.getCreatedAt();
    }

    // Getters và Setters
    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(Integer coinAmount) {
        this.coinAmount = coinAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 