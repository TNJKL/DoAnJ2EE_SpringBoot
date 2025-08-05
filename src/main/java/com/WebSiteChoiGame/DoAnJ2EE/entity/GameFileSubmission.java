package com.WebSiteChoiGame.DoAnJ2EE.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "GameFileSubmissions")
public class GameFileSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer submissionID;

    @ManyToOne
    @JoinColumn(name = "developerID", nullable = false)
    @JsonIgnoreProperties({"challengesAsChallenger", "challengesAsOpponent", "challengesWon", "challengeBets", "betsOnUser", "coinTransactions"})
    private User developer;

    @Column(nullable = false, length = 255)
    private String fileUrl;

    @Column(length = 500)
    private String description;

    @Column(length = 20)
    private String status;

    @Column(length = 500)
    private String adminNote;

    private LocalDateTime submittedAt;

    // Getters and setters
    public Integer getSubmissionID() { return submissionID; }
    public void setSubmissionID(Integer submissionID) { this.submissionID = submissionID; }
    public User getDeveloper() { return developer; }
    public void setDeveloper(User developer) { this.developer = developer; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAdminNote() { return adminNote; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
} 