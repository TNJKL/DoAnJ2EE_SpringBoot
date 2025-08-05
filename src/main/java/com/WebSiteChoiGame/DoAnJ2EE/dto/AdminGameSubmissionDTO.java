package com.WebSiteChoiGame.DoAnJ2EE.dto;

import com.WebSiteChoiGame.DoAnJ2EE.entity.GameFileSubmission;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import java.time.LocalDateTime;

public class AdminGameSubmissionDTO {
    private Integer submissionID;
    private String gameTitle;
    private String gameDescription;
    private String gameFileUrl;
    private String status;
    private String adminNote;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
    
    // Developer info (simplified)
    private Integer developerID;
    private String developerUsername;
    private String developerEmail;

    // Constructor từ GameFileSubmission entity
    public AdminGameSubmissionDTO(GameFileSubmission submission) {
        this.submissionID = submission.getSubmissionID();
        this.gameTitle = submission.getDescription(); // Sử dụng description làm title
        this.gameDescription = submission.getDescription();
        this.gameFileUrl = submission.getFileUrl();
        this.status = submission.getStatus();
        this.adminNote = submission.getAdminNote();
        this.submittedAt = submission.getSubmittedAt();
        this.updatedAt = submission.getSubmittedAt(); // Không có updatedAt, dùng submittedAt
        
        // Lấy thông tin developer đơn giản
        User developer = submission.getDeveloper();
        if (developer != null) {
            this.developerID = developer.getUserID();
            this.developerUsername = developer.getUsername();
            this.developerEmail = developer.getEmail();
        }
    }

    // Getters và Setters
    public Integer getSubmissionID() {
        return submissionID;
    }

    public void setSubmissionID(Integer submissionID) {
        this.submissionID = submissionID;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public String getGameFileUrl() {
        return gameFileUrl;
    }

    public void setGameFileUrl(String gameFileUrl) {
        this.gameFileUrl = gameFileUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getDeveloperID() {
        return developerID;
    }

    public void setDeveloperID(Integer developerID) {
        this.developerID = developerID;
    }

    public String getDeveloperUsername() {
        return developerUsername;
    }

    public void setDeveloperUsername(String developerUsername) {
        this.developerUsername = developerUsername;
    }

    public String getDeveloperEmail() {
        return developerEmail;
    }

    public void setDeveloperEmail(String developerEmail) {
        this.developerEmail = developerEmail;
    }
} 