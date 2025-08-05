package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.ForumPostRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameFileSubmissionRepository;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Game;
import com.WebSiteChoiGame.DoAnJ2EE.entity.ForumPost;
import com.WebSiteChoiGame.DoAnJ2EE.entity.GameFileSubmission;
import com.WebSiteChoiGame.DoAnJ2EE.dto.AdminUserDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.AdminGameDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.AdminGameSubmissionDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.AdminForumPostDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.AdminGameManagementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private GameFileSubmissionRepository gameFileSubmissionRepository;

    // Lấy danh sách user cho admin (không có infinite recursion)
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(AdminUserDTO::new)
            .collect(Collectors.toList());
    }

    // Lấy danh sách game cho admin
    public List<AdminGameDTO> getAllGames(Boolean visible) {
        List<Game> games;
        if (visible == null) {
            games = gameRepository.findAll();
        } else if (visible) {
            games = gameRepository.findAll().stream()
                .filter(g -> g.getIsVisible() == null || g.getIsVisible())
                .toList();
        } else {
            games = gameRepository.findAll().stream()
                .filter(g -> Boolean.FALSE.equals(g.getIsVisible()))
                .toList();
        }
        
        return games.stream()
            .map(game -> {
                AdminGameDTO dto = new AdminGameDTO(game);
                // Đảm bảo thumbnailUrl là đường dẫn đúng
                if (dto.getThumbnailUrl() != null && !dto.getThumbnailUrl().startsWith("/uploads/")) {
                    dto.setThumbnailUrl("/uploads/" + dto.getThumbnailUrl());
                }
                return dto;
            })
            .collect(Collectors.toList());
    }

    // Lấy danh sách game cho admin management (tối ưu performance)
    public List<AdminGameManagementDTO> getAllGamesForManagement(Boolean visible) {
        List<Game> games;
        if (visible == null) {
            games = gameRepository.findAll();
        } else if (visible) {
            games = gameRepository.findAll().stream()
                .filter(g -> g.getIsVisible() == null || g.getIsVisible())
                .toList();
        } else {
            games = gameRepository.findAll().stream()
                .filter(g -> Boolean.FALSE.equals(g.getIsVisible()))
                .toList();
        }
        
        return games.stream()
            .map(AdminGameManagementDTO::new)
            .collect(Collectors.toList());
    }

    // Lấy danh sách user có role dev
    public List<AdminUserDTO> getAllDevUsers() {
        return userRepository.findAllByRole_RoleName("dev").stream()
            .map(AdminUserDTO::new)
            .collect(Collectors.toList());
    }

    // Lấy danh sách game submissions cho admin
    public List<AdminGameSubmissionDTO> getAllGameSubmissions() {
        return gameFileSubmissionRepository.findAll().stream()
            .map(AdminGameSubmissionDTO::new)
            .collect(Collectors.toList());
    }

    // Lấy forum posts theo trạng thái
    public List<AdminForumPostDTO> getForumPostsByStatus(String status) {
        List<ForumPost> allPosts = forumPostRepository.findAll();
        
        switch (status.toLowerCase()) {
            case "pending":
                return allPosts.stream()
                    .filter(post -> post.getIsApproved() == null)
                    .map(AdminForumPostDTO::new)
                    .toList();
            case "approved":
                return allPosts.stream()
                    .filter(post -> Boolean.TRUE.equals(post.getIsApproved()))
                    .map(AdminForumPostDTO::new)
                    .toList();
            case "rejected":
                return allPosts.stream()
                    .filter(post -> Boolean.FALSE.equals(post.getIsApproved()))
                    .map(AdminForumPostDTO::new)
                    .toList();
            default:
                return allPosts.stream()
                    .map(AdminForumPostDTO::new)
                    .toList();
        }
    }
} 