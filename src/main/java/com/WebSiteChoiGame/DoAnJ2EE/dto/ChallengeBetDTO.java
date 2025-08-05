package com.WebSiteChoiGame.DoAnJ2EE.dto;

import java.time.LocalDateTime;

public class ChallengeBetDTO {
    private Integer betID;
    private Integer challengeID;
    private Integer userID;
    private String username;
    private Integer betOnUserID;
    private String betOnUsername;
    private Integer betAmount;
    private Boolean won;
    private Integer payoutAmount;
    private LocalDateTime createdAt;

    // Constructors
    public ChallengeBetDTO() {}

    public ChallengeBetDTO(Integer betID, Integer challengeID, Integer userID, String username,
                          Integer betOnUserID, String betOnUsername, Integer betAmount,
                          Boolean won, Integer payoutAmount, LocalDateTime createdAt) {
        this.betID = betID;
        this.challengeID = challengeID;
        this.userID = userID;
        this.username = username;
        this.betOnUserID = betOnUserID;
        this.betOnUsername = betOnUsername;
        this.betAmount = betAmount;
        this.won = won;
        this.payoutAmount = payoutAmount;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public Integer getBetID() { return betID; }
    public void setBetID(Integer betID) { this.betID = betID; }

    public Integer getChallengeID() { return challengeID; }
    public void setChallengeID(Integer challengeID) { this.challengeID = challengeID; }

    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getBetOnUserID() { return betOnUserID; }
    public void setBetOnUserID(Integer betOnUserID) { this.betOnUserID = betOnUserID; }

    public String getBetOnUsername() { return betOnUsername; }
    public void setBetOnUsername(String betOnUsername) { this.betOnUsername = betOnUsername; }

    public Integer getBetAmount() { return betAmount; }
    public void setBetAmount(Integer betAmount) { this.betAmount = betAmount; }

    public Boolean getWon() { return won; }
    public void setWon(Boolean won) { this.won = won; }

    public Integer getPayoutAmount() { return payoutAmount; }
    public void setPayoutAmount(Integer payoutAmount) { this.payoutAmount = payoutAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 