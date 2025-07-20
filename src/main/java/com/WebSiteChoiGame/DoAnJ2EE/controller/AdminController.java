package com.WebSiteChoiGame.DoAnJ2EE.controller;

import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.ForumPostRepository;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Role;
import com.WebSiteChoiGame.DoAnJ2EE.repository.RoleRepository;
import com.WebSiteChoiGame.DoAnJ2EE.entity.GameFileSubmission;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameFileSubmissionRepository;
import com.WebSiteChoiGame.DoAnJ2EE.entity.ForumPost;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameGenreRepository;
import com.WebSiteChoiGame.DoAnJ2EE.entity.GameGenre;
import com.WebSiteChoiGame.DoAnJ2EE.entity.GenreDTO;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ForumPostRepository forumPostRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private GameFileSubmissionRepository gameFileSubmissionRepository;
    @Autowired
    private GameGenreRepository gameGenreRepository;

    @GetMapping("/summary")
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalUsers", userRepository.count());
        result.put("totalGames", gameRepository.count());
        result.put("totalForumPosts", forumPostRepository.count());
        return result;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/game-submissions")
    public List<GameFileSubmission> getAllGameSubmissions() {
        return gameFileSubmissionRepository.findAll();
    }

    @GetMapping("/forum-posts/pending")
    public List<ForumPost> getPendingForumPosts() {
        return forumPostRepository.findAll().stream()
            .filter(post -> post.getIsApproved() == null)
            .toList();
    }

    @GetMapping("/forum-posts/approved")
    public List<ForumPost> getApprovedForumPosts() {
        return forumPostRepository.findAll().stream()
            .filter(post -> Boolean.TRUE.equals(post.getIsApproved()))
            .toList();
    }

    @GetMapping("/forum-posts/rejected")
    public List<ForumPost> getRejectedForumPosts() {
        return forumPostRepository.findAll().stream()
            .filter(post -> Boolean.FALSE.equals(post.getIsApproved()))
            .toList();
    }

    // Lấy danh sách game (có thể filter ẩn/hiện)
    @GetMapping("/games")
    public List<Game> getAllGames(@RequestParam(value = "visible", required = false) Boolean visible) {
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
        // Đảm bảo thumbnailUrl là đường dẫn đúng
        for (Game g : games) {
            if (g.getThumbnailUrl() != null && !g.getThumbnailUrl().startsWith("/uploads/")) {
                g.setThumbnailUrl("/uploads/" + g.getThumbnailUrl());
            }
        }
        return games;
    }

    // Thêm game mới
    @PostMapping("/games")
    public Game createGame(@RequestBody Game game) {
        game.setIsVisible(true);
        game.setIsApproved(true);
        // Nếu không có gameType thì mặc định là placeholder
        if (game.getGameType() == null || game.getGameType().isEmpty()) {
            game.setGameType("placeholder");
        }
        if (game.getCreatedAt() == null) {
            game.setCreatedAt(java.time.LocalDateTime.now());
        }
        return gameRepository.save(game);
    }

    // Sửa game
    @PutMapping("/games/{id}")
    public Game updateGame(@PathVariable Integer id, @RequestBody Game req) {
        Game game = gameRepository.findById(id).orElseThrow();
        game.setTitle(req.getTitle());
        game.setDescription(req.getDescription());
        game.setThumbnailUrl(req.getThumbnailUrl());
        game.setGameUrl(req.getGameUrl());
        game.setGameType(req.getGameType());
        // Nếu muốn sửa genre, createdBy thì cần xử lý thêm
        if (req.getGenre() != null && req.getGenre().getGenreID() != null) {
            GameGenre genre = gameGenreRepository.findById(req.getGenre().getGenreID()).orElseThrow();
            game.setGenre(genre);
        }
        if (req.getCreatedBy() != null && req.getCreatedBy().getUserID() != null) {
            User user = userRepository.findById(req.getCreatedBy().getUserID()).orElseThrow();
            game.setCreatedBy(user);
        }
        return gameRepository.save(game);
    }

    // Ẩn game (không xóa khỏi DB)
    @PutMapping("/games/{id}/hide")
    public void hideGame(@PathVariable Integer id) {
        Game game = gameRepository.findById(id).orElseThrow();
        game.setIsVisible(false);
        gameRepository.save(game);
    }

    // Hiện lại game
    @PutMapping("/games/{id}/show")
    public void showGame(@PathVariable Integer id) {
        Game game = gameRepository.findById(id).orElseThrow();
        game.setIsVisible(true);
        gameRepository.save(game);
    }

    public static class UserRequest {
        @NotBlank(message = "Tên đăng nhập không được để trống")
        public String username;
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        public String email;
        public String password;
        @NotBlank(message = "Role không được để trống")
        public String role;
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody UserRequest req) {
        User user = new User();
        user.setUsername(req.username);
        user.setEmail(req.email);
        if (req.password != null && !req.password.isEmpty()) {
            user.setPasswordHash(new BCryptPasswordEncoder().encode(req.password));
        }
        Role role = roleRepository.findByRoleName(req.role).orElseThrow(() -> new RuntimeException("Role không hợp lệ!"));
        user.setRole(role);
        user.setCreatedAt(java.time.LocalDateTime.now());
        return userRepository.save(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id, @Valid @RequestBody UserRequest req) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(req.username);
        user.setEmail(req.email);
        if (req.password != null && !req.password.isEmpty()) {
            user.setPasswordHash(new BCryptPasswordEncoder().encode(req.password));
        }
        Role role = roleRepository.findByRoleName(req.role).orElseThrow(() -> new RuntimeException("Role không hợp lệ!"));
        user.setRole(role);
        return userRepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

    @PutMapping("/game-submissions/{id}/approve")
    public void approveGameSubmission(@PathVariable Integer id) {
        GameFileSubmission sub = gameFileSubmissionRepository.findById(id).orElseThrow();
        sub.setStatus("Approved");
        gameFileSubmissionRepository.save(sub);
    }

    @PutMapping("/game-submissions/{id}/reject")
    public void rejectGameSubmission(@PathVariable Integer id, @RequestBody Map<String, String> req) {
        GameFileSubmission sub = gameFileSubmissionRepository.findById(id).orElseThrow();
        sub.setStatus("Rejected");
        sub.setAdminNote(req.getOrDefault("adminNote", ""));
        gameFileSubmissionRepository.save(sub);
    }

    @PutMapping("/forum-posts/{id}/approve")
    public void approveForumPost(@PathVariable Integer id) {
        ForumPost post = forumPostRepository.findById(id).orElseThrow();
        post.setIsApproved(true);
        forumPostRepository.save(post);
    }

    @PutMapping("/forum-posts/{id}/reject")
    public void rejectForumPost(@PathVariable Integer id) {
        ForumPost post = forumPostRepository.findById(id).orElseThrow();
        post.setIsApproved(false);
        forumPostRepository.save(post);
    }

    // API upload file ảnh
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File rỗng");
        }
        try {
            // Đường dẫn lưu file (ví dụ: src/main/resources/static/uploads)
            String uploadDir = "src/main/resources/static/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // Tạo tên file duy nhất
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());

            // Đường dẫn public để frontend truy cập
            String fileUrl = "/uploads/" + fileName;

            // Trả về URL cho frontend
            return ResponseEntity.ok().body(new java.util.HashMap<String, String>() {{
                put("url", fileUrl);
            }});
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi upload file");
        }
    }

    // API lấy danh sách thể loại game
    @GetMapping("/game-genres")
    public List<GenreDTO> getAllGameGenres() {
        return gameGenreRepository.findAll().stream()
            .map(g -> new GenreDTO(g.getGenreID(), g.getName()))
            .toList();
    }

    // API lấy danh sách user có role dev
    @GetMapping("/dev-users")
    public List<User> getAllDevUsers() {
        return userRepository.findAllByRole_RoleName("dev");
    }
} 