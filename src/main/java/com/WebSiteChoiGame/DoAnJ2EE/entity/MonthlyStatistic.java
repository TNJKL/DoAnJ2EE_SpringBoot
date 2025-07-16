package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MonthlyStatistics")
public class MonthlyStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer monthID;

    @Column(length = 7)
    private String monthYear;

    private Integer totalUsers;
    private Integer totalGames;
    private Integer totalGamePlays;

    @ManyToOne
    @JoinColumn(name = "topUserID")
    private User topUser;

    private Integer topUserScore;

    @ManyToOne
    @JoinColumn(name = "topGameID")
    private Game topGame;

    private Integer topGamePlayCount;
    private Integer totalForumPosts;
    private LocalDateTime createdAt;

    // Getters and setters
    public Integer getMonthID() { return monthID; }
    public void setMonthID(Integer monthID) { this.monthID = monthID; }
    public String getMonthYear() { return monthYear; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }
    public Integer getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Integer totalUsers) { this.totalUsers = totalUsers; }
    public Integer getTotalGames() { return totalGames; }
    public void setTotalGames(Integer totalGames) { this.totalGames = totalGames; }
    public Integer getTotalGamePlays() { return totalGamePlays; }
    public void setTotalGamePlays(Integer totalGamePlays) { this.totalGamePlays = totalGamePlays; }
    public User getTopUser() { return topUser; }
    public void setTopUser(User topUser) { this.topUser = topUser; }
    public Integer getTopUserScore() { return topUserScore; }
    public void setTopUserScore(Integer topUserScore) { this.topUserScore = topUserScore; }
    public Game getTopGame() { return topGame; }
    public void setTopGame(Game topGame) { this.topGame = topGame; }
    public Integer getTopGamePlayCount() { return topGamePlayCount; }
    public void setTopGamePlayCount(Integer topGamePlayCount) { this.topGamePlayCount = topGamePlayCount; }
    public Integer getTotalForumPosts() { return totalForumPosts; }
    public void setTotalForumPosts(Integer totalForumPosts) { this.totalForumPosts = totalForumPosts; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 