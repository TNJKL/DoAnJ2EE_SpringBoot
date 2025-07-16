package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ForumComments")
public class ForumComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentID;

    @ManyToOne
    @JoinColumn(name = "postID", nullable = false)
    private ForumPost post;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String content;

    private LocalDateTime createdAt;

    // Getters and setters
    public Integer getCommentID() { return commentID; }
    public void setCommentID(Integer commentID) { this.commentID = commentID; }
    public ForumPost getPost() { return post; }
    public void setPost(ForumPost post) { this.post = post; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 