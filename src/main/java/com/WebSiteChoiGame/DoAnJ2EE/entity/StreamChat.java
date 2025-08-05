package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "StreamChat")
public class StreamChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ChatID")
    private Integer chatId;

    @ManyToOne
    @JoinColumn(name = "StreamID", nullable = false)
    private Stream stream;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column(name = "Message", nullable = false, length = 500)
    private String message;

    @Column(name = "SentAt")
    private LocalDateTime sentAt = LocalDateTime.now();

    // Constructors
    public StreamChat() {}

    public StreamChat(Stream stream, User user, String message) {
        this.stream = stream;
        this.user = user;
        this.message = message;
    }

    // Getters and Setters
    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
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