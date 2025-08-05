package com.WebSiteChoiGame.DoAnJ2EE.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    private String passwordHash;
    private String googleID;

    @ManyToOne
    @JoinColumn(name = "RoleID", nullable = false)
    @JsonIgnoreProperties("users")
    private Role role;

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private List<Game> games;

    @OneToOne(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private UserCoins userCoins;

    @OneToMany(mappedBy = "challenger")
    @JsonIgnoreProperties("challenger")
    private List<Challenge> challengesAsChallenger;

    @OneToMany(mappedBy = "opponent")
    @JsonIgnoreProperties("opponent")
    private List<Challenge> challengesAsOpponent;

    @OneToMany(mappedBy = "winner")
    @JsonIgnoreProperties("winner")
    private List<Challenge> challengesWon;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<ChallengeBet> challengeBets;

    @OneToMany(mappedBy = "betOnUser")
    @JsonIgnoreProperties("betOnUser")
    private List<ChallengeBet> betsOnUser;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<CoinTransaction> coinTransactions;

    private LocalDateTime createdAt;

    // Getters and setters
    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getGoogleID() { return googleID; }
    public void setGoogleID(String googleID) { this.googleID = googleID; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public List<Game> getGames() { return games; }
    public void setGames(List<Game> games) { this.games = games; }
    public UserCoins getUserCoins() { return userCoins; }
    public void setUserCoins(UserCoins userCoins) { this.userCoins = userCoins; }
    public List<Challenge> getChallengesAsChallenger() { return challengesAsChallenger; }
    public void setChallengesAsChallenger(List<Challenge> challengesAsChallenger) { this.challengesAsChallenger = challengesAsChallenger; }
    public List<Challenge> getChallengesAsOpponent() { return challengesAsOpponent; }
    public void setChallengesAsOpponent(List<Challenge> challengesAsOpponent) { this.challengesAsOpponent = challengesAsOpponent; }
    public List<Challenge> getChallengesWon() { return challengesWon; }
    public void setChallengesWon(List<Challenge> challengesWon) { this.challengesWon = challengesWon; }
    public List<ChallengeBet> getChallengeBets() { return challengeBets; }
    public void setChallengeBets(List<ChallengeBet> challengeBets) { this.challengeBets = challengeBets; }
    public List<ChallengeBet> getBetsOnUser() { return betsOnUser; }
    public void setBetsOnUser(List<ChallengeBet> betsOnUser) { this.betsOnUser = betsOnUser; }
    public List<CoinTransaction> getCoinTransactions() { return coinTransactions; }
    public void setCoinTransactions(List<CoinTransaction> coinTransactions) { this.coinTransactions = coinTransactions; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 