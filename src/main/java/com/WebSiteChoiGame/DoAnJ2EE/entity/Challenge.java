package com.WebSiteChoiGame.DoAnJ2EE.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Challenges")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer challengeID;

    @ManyToOne
    @JoinColumn(name = "ChallengerID", nullable = false)
    @JsonIgnoreProperties("challengesAsChallenger")
    private User challenger;

    @ManyToOne
    @JoinColumn(name = "OpponentID", nullable = false)
    @JsonIgnoreProperties("challengesAsOpponent")
    private User opponent;

    @ManyToOne
    @JoinColumn(name = "GameID", nullable = false)
    @JsonIgnoreProperties("challenges")
    private Game game;

    @Column(name = "BetAmount", nullable = false)
    private Integer betAmount;

    @Column(name = "Status", length = 20)
    private String status = "pending"; // pending/accepted/declined/active/finished

    @Column(name = "StartTime")
    private LocalDateTime startTime;

    @Column(name = "EndTime")
    private LocalDateTime endTime;

    @Column(name = "ChallengerScore")
    private Integer challengerScore;

    @Column(name = "OpponentScore")
    private Integer opponentScore;

    @ManyToOne
    @JoinColumn(name = "WinnerID")
    @JsonIgnoreProperties("challengesWon")
    private User winner;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "challenge")
    @JsonIgnoreProperties("challenge")
    private List<ChallengeBet> bets;

    @OneToMany(mappedBy = "relatedChallenge")
    @JsonIgnoreProperties("relatedChallenge")
    private List<CoinTransaction> coinTransactions;

    // Constructors
    public Challenge() {}

    public Challenge(User challenger, User opponent, Game game, Integer betAmount) {
        this.challenger = challenger;
        this.opponent = opponent;
        this.game = game;
        this.betAmount = betAmount;
        this.status = "pending";
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Integer getChallengeID() { return challengeID; }
    public void setChallengeID(Integer challengeID) { this.challengeID = challengeID; }

    public User getChallenger() { return challenger; }
    public void setChallenger(User challenger) { this.challenger = challenger; }

    public User getOpponent() { return opponent; }
    public void setOpponent(User opponent) { this.opponent = opponent; }

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }

    public Integer getBetAmount() { return betAmount; }
    public void setBetAmount(Integer betAmount) { this.betAmount = betAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Integer getChallengerScore() { return challengerScore; }
    public void setChallengerScore(Integer challengerScore) { this.challengerScore = challengerScore; }

    public Integer getOpponentScore() { return opponentScore; }
    public void setOpponentScore(Integer opponentScore) { this.opponentScore = opponentScore; }

    public User getWinner() { return winner; }
    public void setWinner(User winner) { this.winner = winner; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ChallengeBet> getBets() { return bets; }
    public void setBets(List<ChallengeBet> bets) { this.bets = bets; }

    public List<CoinTransaction> getCoinTransactions() { return coinTransactions; }
    public void setCoinTransactions(List<CoinTransaction> coinTransactions) { this.coinTransactions = coinTransactions; }
} 