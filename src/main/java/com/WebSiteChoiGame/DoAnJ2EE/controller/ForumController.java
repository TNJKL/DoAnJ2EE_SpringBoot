package com.WebSiteChoiGame.DoAnJ2EE.controller;

import com.WebSiteChoiGame.DoAnJ2EE.entity.ForumPost;
import com.WebSiteChoiGame.DoAnJ2EE.entity.ForumComment;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.repository.ForumPostRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.ForumCommentRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/forum")
public class ForumController {
    @Autowired private ForumPostRepository postRepo;
    @Autowired private ForumCommentRepository commentRepo;
    @Autowired private UserRepository userRepo;

    // DTO cho bài viết
    public static class ForumPostDTO {
        public Integer postID;
        public String username;
        public String title;
        public String content;
        public String imageUrl;
        public LocalDateTime createdAt;
        public Boolean isApproved; // Thêm trường này
        public List<ForumCommentDTO> comments;
    }
    // DTO cho bình luận
    public static class ForumCommentDTO {
        public Integer commentID;
        public String username;
        public String content;
        public LocalDateTime createdAt;
    }

    // Lấy danh sách bài viết (feed)
    @GetMapping("/posts")
    public List<ForumPostDTO> getAllPosts() {
        return postRepo.findAll().stream().map(this::toPostDTO).collect(Collectors.toList());
    }

    // Lấy chi tiết bài viết (kèm bình luận)
    @GetMapping("/posts/{id}")
    public ForumPostDTO getPost(@PathVariable Integer id) {
        ForumPost post = postRepo.findById(id).orElseThrow();
        return toPostDTO(post);
    }

    // Thêm bài viết
    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody ForumPostDTO req, @RequestHeader("username") String username) {
        User user = userRepo.findByUsername(username).orElseThrow();
        ForumPost post = new ForumPost();
        post.setUser(user);
        post.setTitle(req.title);
        post.setContent(req.content);
        post.setImageUrl(req.imageUrl);
        post.setCreatedAt(LocalDateTime.now());
        post.setIsApproved(null); // Đảm bảo bài viết mới có trạng thái chờ duyệt
        postRepo.save(post);
        return ResponseEntity.ok(toPostDTO(post));
    }

    // Sửa bài viết (chỉ chủ bài viết mới được sửa)
    @PutMapping("/posts/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Integer id, @RequestBody ForumPostDTO req, @RequestHeader("username") String username) {
        User user = userRepo.findByUsername(username).orElseThrow();
        ForumPost post = postRepo.findById(id).orElseThrow();
        if (!post.getUser().getUserID().equals(user.getUserID())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền sửa bài viết này!");
        }
        post.setTitle(req.title);
        post.setContent(req.content);
        post.setImageUrl(req.imageUrl);
        postRepo.save(post);
        return ResponseEntity.ok(toPostDTO(post));
    }

    // Xóa bài viết (chỉ chủ bài viết mới được xóa)
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id, @RequestHeader("username") String username) {
        User user = userRepo.findByUsername(username).orElseThrow();
        ForumPost post = postRepo.findById(id).orElseThrow();
        if (!post.getUser().getUserID().equals(user.getUserID())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền xóa bài viết này!");
        }
        postRepo.delete(post);
        return ResponseEntity.ok("Đã xóa bài viết");
    }

    // Upload ảnh cho bài viết
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File rỗng");
        }
        try {
            String uploadDir = "src/main/resources/static/uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());
            String fileUrl = "/uploads/" + fileName;
            return ResponseEntity.ok().body(new java.util.HashMap<String, String>() {{
                put("url", fileUrl);
            }});
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi upload file");
        }
    }

    // Lấy bình luận theo post
    @GetMapping("/posts/{postId}/comments")
    public List<ForumCommentDTO> getComments(@PathVariable Integer postId) {
        ForumPost post = postRepo.findById(postId).orElseThrow();
        return post.getComments().stream().map(this::toCommentDTO).collect(Collectors.toList());
    }

    // Thêm bình luận
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Integer postId, @RequestBody ForumCommentDTO req, @RequestHeader("username") String username) {
        User user = userRepo.findByUsername(username).orElseThrow();
        ForumPost post = postRepo.findById(postId).orElseThrow();
        ForumComment cmt = new ForumComment();
        cmt.setPost(post);
        cmt.setUser(user);
        cmt.setContent(req.content);
        cmt.setCreatedAt(LocalDateTime.now());
        commentRepo.save(cmt);
        return ResponseEntity.ok(toCommentDTO(cmt));
    }

    // Sửa bình luận (chỉ chủ bình luận mới được sửa)
    @PutMapping("/comments/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Integer id, @RequestBody ForumCommentDTO req, @RequestHeader("username") String username) {
        User user = userRepo.findByUsername(username).orElseThrow();
        ForumComment cmt = commentRepo.findById(id).orElseThrow();
        if (!cmt.getUser().getUserID().equals(user.getUserID())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền sửa bình luận này!");
        }
        cmt.setContent(req.content);
        commentRepo.save(cmt);
        return ResponseEntity.ok(toCommentDTO(cmt));
    }

    // Xóa bình luận (chỉ chủ bình luận mới được xóa)
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id, @RequestHeader("username") String username) {
        User user = userRepo.findByUsername(username).orElseThrow();
        ForumComment cmt = commentRepo.findById(id).orElseThrow();
        if (!cmt.getUser().getUserID().equals(user.getUserID())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền xóa bình luận này!");
        }
        commentRepo.delete(cmt);
        return ResponseEntity.ok("Đã xóa bình luận");
    }

    // Helper chuyển entity sang DTO
    private ForumPostDTO toPostDTO(ForumPost post) {
        ForumPostDTO dto = new ForumPostDTO();
        dto.postID = post.getPostID();
        dto.username = post.getUser().getUsername();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.imageUrl = post.getImageUrl();
        dto.createdAt = post.getCreatedAt();
        dto.isApproved = post.getIsApproved(); // Thêm dòng này
        dto.comments = post.getComments() == null ? List.of() : post.getComments().stream().map(this::toCommentDTO).collect(Collectors.toList());
        return dto;
    }
    private ForumCommentDTO toCommentDTO(ForumComment cmt) {
        ForumCommentDTO dto = new ForumCommentDTO();
        dto.commentID = cmt.getCommentID();
        dto.username = cmt.getUser().getUsername();
        dto.content = cmt.getContent();
        dto.createdAt = cmt.getCreatedAt();
        return dto;
    }
} 