package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserSessions")
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String token;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    // Getters and setters
    public Integer getSessionID() { return sessionID; }
    public void setSessionID(Integer sessionID) { this.sessionID = sessionID; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getExpiredAt() { return expiredAt; }
    public void setExpiredAt(LocalDateTime expiredAt) { this.expiredAt = expiredAt; }
} 