package com.WebSiteChoiGame.DoAnJ2EE.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CoinTransactions")
public class CoinTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionID;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    @JsonIgnoreProperties("coinTransactions")
    private User user;

    @Column(name = "TransactionType", nullable = false, length = 50)
    private String transactionType; // bet, win, lose, admin_add, admin_subtract

    @Column(name = "Amount", nullable = false)
    private Integer amount; // Số coin thay đổi (dương = nhận, âm = mất)

    @Column(name = "BalanceAfter", nullable = false)
    private Integer balanceAfter; // Số dư sau giao dịch

    @Column(name = "Description", length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "RelatedChallengeID")
    @JsonIgnoreProperties("coinTransactions")
    private Challenge relatedChallenge;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    // Constructors
    public CoinTransaction() {}

    public CoinTransaction(User user, String transactionType, Integer amount, 
                         Integer balanceAfter, String description) {
        this.user = user;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Integer getTransactionID() { return transactionID; }
    public void setTransactionID(Integer transactionID) { this.transactionID = transactionID; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public Integer getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(Integer balanceAfter) { this.balanceAfter = balanceAfter; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Challenge getRelatedChallenge() { return relatedChallenge; }
    public void setRelatedChallenge(Challenge relatedChallenge) { this.relatedChallenge = relatedChallenge; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 