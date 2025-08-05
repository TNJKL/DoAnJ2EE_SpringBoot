package com.WebSiteChoiGame.DoAnJ2EE.dto;

import java.time.LocalDateTime;

public class StreamChatDTO {
    private Integer chatId;
    private Integer streamId;
    private Integer userId;
    private String username;
    private String message;
    private LocalDateTime sentAt;

    // Constructors
    public StreamChatDTO() {}

    public StreamChatDTO(Integer chatId, Integer streamId, Integer userId, 
                        String username, String message, LocalDateTime sentAt) {
        this.chatId = chatId;
        this.streamId = streamId;
        this.userId = userId;
        this.username = username;
        this.message = message;
        this.sentAt = sentAt;
    }

    // Getters and Setters
    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getStreamId() {
        return streamId;
    }

    public void setStreamId(Integer streamId) {
        this.streamId = streamId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
} 