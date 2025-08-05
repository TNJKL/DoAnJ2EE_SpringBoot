package com.WebSiteChoiGame.DoAnJ2EE.dto;

import java.time.LocalDateTime;

public class CoinTransactionDTO {
    private Integer transactionID;
    private Integer userID;
    private String username;
    private String transactionType;
    private Integer amount;
    private Integer balanceAfter;
    private String description;
    private Integer relatedChallengeID;
    private LocalDateTime createdAt;

    // Constructors
    public CoinTransactionDTO() {}

    public CoinTransactionDTO(Integer transactionID, Integer userID, String username, 
                            String transactionType, Integer amount, Integer balanceAfter, 
                            String description, Integer relatedChallengeID, LocalDateTime createdAt) {
        this.transactionID = transactionID;
        this.userID = userID;
        this.username = username;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.relatedChallengeID = relatedChallengeID;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public Integer getTransactionID() { return transactionID; }
    public void setTransactionID(Integer transactionID) { this.transactionID = transactionID; }

    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public Integer getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(Integer balanceAfter) { this.balanceAfter = balanceAfter; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getRelatedChallengeID() { return relatedChallengeID; }
    public void setRelatedChallengeID(Integer relatedChallengeID) { this.relatedChallengeID = relatedChallengeID; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 