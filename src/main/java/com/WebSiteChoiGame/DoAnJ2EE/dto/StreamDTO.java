package com.WebSiteChoiGame.DoAnJ2EE.dto;

import java.time.LocalDateTime;

public class StreamDTO {
    private Integer streamId;
    private Integer streamerId;
    private String streamerUsername;
    private Integer gameId;
    private String gameTitle;
    private String title;
    private String description;
    private String streamKey;
    private Boolean isLive;
    private Integer viewerCount;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;

    // Constructors
    public StreamDTO() {}

    public StreamDTO(Integer streamId, Integer streamerId, String streamerUsername, 
                    Integer gameId, String gameTitle, String title, String description, 
                    String streamKey, Boolean isLive, Integer viewerCount) {
        this.streamId = streamId;
        this.streamerId = streamerId;
        this.streamerUsername = streamerUsername;
        this.gameId = gameId;
        this.gameTitle = gameTitle;
        this.title = title;
        this.description = description;
        this.streamKey = streamKey;
        this.isLive = isLive;
        this.viewerCount = viewerCount;
    }

    // Getters and Setters
    public Integer getStreamId() {
        return streamId;
    }

    public void setStreamId(Integer streamId) {
        this.streamId = streamId;
    }

    public Integer getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(Integer streamerId) {
        this.streamerId = streamerId;
    }

    public String getStreamerUsername() {
        return streamerUsername;
    }

    public void setStreamerUsername(String streamerUsername) {
        this.streamerUsername = streamerUsername;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
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