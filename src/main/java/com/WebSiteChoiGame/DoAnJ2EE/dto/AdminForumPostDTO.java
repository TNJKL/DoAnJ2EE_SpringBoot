package com.WebSiteChoiGame.DoAnJ2EE.dto;

import com.WebSiteChoiGame.DoAnJ2EE.entity.ForumPost;
import java.time.LocalDateTime;

public class AdminForumPostDTO {
    private Integer postID;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Boolean isApproved;
    private String username; // Chỉ lấy username thay vì toàn bộ User object

    public AdminForumPostDTO(ForumPost post) {
        this.postID = post.getPostID();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.createdAt = post.getCreatedAt();
        this.isApproved = post.getIsApproved();
        this.username = post.getUser() != null ? post.getUser().getUsername() : null;
    }

    // Getters và Setters
    public Integer getPostID() { return postID; }
    public void setPostID(Integer postID) { this.postID = postID; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
} 