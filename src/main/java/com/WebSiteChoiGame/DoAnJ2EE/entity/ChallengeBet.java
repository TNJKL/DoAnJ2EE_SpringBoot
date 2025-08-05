package com.WebSiteChoiGame.DoAnJ2EE.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ChallengeBets")
public class ChallengeBet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer betID;

    @ManyToOne
    @JoinColumn(name = "ChallengeID", nullable = false)
    @JsonIgnoreProperties("bets")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    @JsonIgnoreProperties("challengeBets")
    private User user;

    @ManyToOne
    @JoinColumn(name = "BetOnUserID", nullable = false)
    @JsonIgnoreProperties("betsOnUser")
    private User betOnUser; // Đặt cược cho ai

    @Column(name = "BetAmount", nullable = false)
    private Integer betAmount;

    @Column(name = "Won")
    private Boolean won; // Có thắng không

    @Column(name = "PayoutAmount")
    private Integer payoutAmount; // Số coin nhận được

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    // Constructors
    public ChallengeBet() {}

    public ChallengeBet(Challenge challenge, User user, User betOnUser, Integer betAmount) {
        this.challenge = challenge;
        this.user = user;
        this.betOnUser = betOnUser;
        this.betAmount = betAmount;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Integer getBetID() { return betID; }
    public void setBetID(Integer betID) { this.betID = betID; }

    public Challenge getChallenge() { return challenge; }
    public void setChallenge(Challenge challenge) { this.challenge = challenge; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getBetOnUser() { return betOnUser; }
    public void setBetOnUser(User betOnUser) { this.betOnUser = betOnUser; }

    public Integer getBetAmount() { return betAmount; }
    public void setBetAmount(Integer betAmount) { this.betAmount = betAmount; }

    public Boolean getWon() { return won; }
    public void setWon(Boolean won) { this.won = won; }

    public Integer getPayoutAmount() { return payoutAmount; }
    public void setPayoutAmount(Integer payoutAmount) { this.payoutAmount = payoutAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 