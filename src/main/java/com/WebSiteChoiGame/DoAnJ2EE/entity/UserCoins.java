package com.WebSiteChoiGame.DoAnJ2EE.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserCoins")
public class UserCoins {
    @Id
    private Integer userID;

    @Column(name = "CoinAmount", nullable = false)
    private Integer coinAmount = 1000;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "UserID")
    @JsonIgnoreProperties("userCoins")
    private User user;

    // Constructors
    public UserCoins() {}

    public UserCoins(User user, Integer coinAmount) {
        this.user = user;
        this.userID = user.getUserID();
        this.coinAmount = coinAmount;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and setters
    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }

    public Integer getCoinAmount() { return coinAmount; }
    public void setCoinAmount(Integer coinAmount) { 
        this.coinAmount = coinAmount; 
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
} 