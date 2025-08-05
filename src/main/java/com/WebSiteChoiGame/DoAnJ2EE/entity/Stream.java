package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Streams")
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StreamID")
    private Integer streamId;

    @ManyToOne
    @JoinColumn(name = "StreamerID", nullable = false)
    private User streamer;

    @ManyToOne
    @JoinColumn(name = "GameID", nullable = false)
    private Game game;

    @Column(name = "Title", nullable = false, length = 200)
    private String title;

    @Column(name = "Description", length = 500)
    private String description;

    @Column(name = "StreamKey", nullable = false, unique = true, length = 255)
    private String streamKey;

    @Column(name = "IsLive")
    private Boolean isLive = false;

    @Column(name = "ViewerCount")
    private Integer viewerCount = 0;

    @Column(name = "StartedAt")
    private LocalDateTime startedAt;

    @Column(name = "EndedAt")
    private LocalDateTime endedAt;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public Stream() {}

    public Stream(User streamer, Game game, String title, String description, String streamKey) {
        this.streamer = streamer;
        this.game = game;
        this.title = title;
        this.description = description;
        this.streamKey = streamKey;
    }

    // Getters and Setters
    public Integer getStreamId() {
        return streamId;
    }

    public void setStreamId(Integer streamId) {
        this.streamId = streamId;
    }

    public User getStreamer() {
        return streamer;
    }

    public void setStreamer(User streamer) {
        this.streamer = streamer;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStreamKey() {
        return streamKey;
    }

    public void setStreamKey(String streamKey) {
        this.streamKey = streamKey;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Integer getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(Integer viewerCount) {
        this.viewerCount = viewerCount;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 