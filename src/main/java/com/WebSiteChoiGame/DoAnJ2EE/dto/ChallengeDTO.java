package com.WebSiteChoiGame.DoAnJ2EE.dto;

import java.time.LocalDateTime;

public class ChallengeDTO {
    private Integer challengeID;
    private Integer challengerID;
    private String challengerUsername;
    private Integer opponentID;
    private String opponentUsername;
    private Integer gameID;
    private String gameTitle;
    private Integer betAmount;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer challengerScore;
    private Integer opponentScore;
    private Integer winnerID;
    private String winnerUsername;
    private LocalDateTime createdAt;
    
    // Thêm thông tin cho betting
    private Integer totalBets;
    private Integer challengerBets;
    private Integer opponentBets;
    private Boolean canBet;
    private Long timeRemaining; // Thời gian còn lại để đặt cược (milliseconds)

    // Constructors
    public ChallengeDTO() {}

    public ChallengeDTO(Integer challengeID, Integer challengerID, String challengerUsername,
                       Integer opponentID, String opponentUsername, Integer gameID, String gameTitle,
                       Integer betAmount, String status, LocalDateTime startTime, LocalDateTime endTime,
                       Integer challengerScore, Integer opponentScore, Integer winnerID, String winnerUsername,
                       LocalDateTime createdAt) {
        this.challengeID = challengeID;
        this.challengerID = challengerID;
        this.challengerUsername = challengerUsername;
        this.opponentID = opponentID;
        this.opponentUsername = opponentUsername;
        this.gameID = gameID;
        this.gameTitle = gameTitle;
        this.betAmount = betAmount;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.challengerScore = challengerScore;
        this.opponentScore = opponentScore;
        this.winnerID = winnerID;
        this.winnerUsername = winnerUsername;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public Integer getChallengeID() { return challengeID; }
    public void setChallengeID(Integer challengeID) { this.challengeID = challengeID; }

    public Integer getChallengerID() { return challengerID; }
    public void setChallengerID(Integer challengerID) { this.challengerID = challengerID; }

    public String getChallengerUsername() { return challengerUsername; }
    public void setChallengerUsername(String challengerUsername) { this.challengerUsername = challengerUsername; }

    public Integer getOpponentID() { return opponentID; }
    public void setOpponentID(Integer opponentID) { this.opponentID = opponentID; }

    public String getOpponentUsername() { return opponentUsername; }
    public void setOpponentUsername(String opponentUsername) { this.opponentUsername = opponentUsername; }

    public Integer getGameID() { return gameID; }
    public void setGameID(Integer gameID) { this.gameID = gameID; }

    public String getGameTitle() { return gameTitle; }
    public void setGameTitle(String gameTitle) { this.gameTitle = gameTitle; }

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

    public Integer getWinnerID() { return winnerID; }
    public void setWinnerID(Integer winnerID) { this.winnerID = winnerID; }

    public String getWinnerUsername() { return winnerUsername; }
    public void setWinnerUsername(String winnerUsername) { this.winnerUsername = winnerUsername; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getTotalBets() { return totalBets; }
    public void setTotalBets(Integer totalBets) { this.totalBets = totalBets; }

    public Integer getChallengerBets() { return challengerBets; }
    public void setChallengerBets(Integer challengerBets) { this.challengerBets = challengerBets; }

    public Integer getOpponentBets() { return opponentBets; }
    public void setOpponentBets(Integer opponentBets) { this.opponentBets = opponentBets; }

    public Boolean getCanBet() { return canBet; }
    public void setCanBet(Boolean canBet) { this.canBet = canBet; }

    public Long getTimeRemaining() { return timeRemaining; }
    public void setTimeRemaining(Long timeRemaining) { this.timeRemaining = timeRemaining; }
} 