package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "StreamViewers")
public class StreamViewer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ViewerID")
    private Integer viewerId;

    @ManyToOne
    @JoinColumn(name = "StreamID", nullable = false)
    private Stream stream;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column(name = "JoinedAt")
    private LocalDateTime joinedAt = LocalDateTime.now();

    @Column(name = "LeftAt")
    private LocalDateTime leftAt;

    // Constructors
    public StreamViewer() {}

    public StreamViewer(Stream stream, User user) {
        this.stream = stream;
        this.user = user;
    }

    // Getters and Setters
    public Integer getViewerId() {
        return viewerId;
    }

    public void setViewerId(Integer viewerId) {
        this.viewerId = viewerId;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getLeftAt() {
        return leftAt;
    }

    public void setLeftAt(LocalDateTime leftAt) {
        this.leftAt = leftAt;
    }
} 