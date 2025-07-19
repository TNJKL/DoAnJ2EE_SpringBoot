package com.WebSiteChoiGame.DoAnJ2EE.controller;

import com.WebSiteChoiGame.DoAnJ2EE.dto.DevFileSubmissionDTO;
import com.WebSiteChoiGame.DoAnJ2EE.entity.GameFileSubmission;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameFileSubmissionRepository;
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
@RequestMapping("/api/dev")
public class DevFileSubmissionController {
    @Autowired
    private GameFileSubmissionRepository submissionRepo;
    @Autowired
    private UserRepository userRepo;

    // DTO trả về cho frontend
    public static class DevFileSubmissionResponseDTO {
        public Integer submissionID;
        public String developerUsername;
        public String fileUrl;
        public String description;
        public String status;
        public String adminNote;
        public LocalDateTime submittedAt;
    }

    // Lấy danh sách submission của dev hiện tại
    @GetMapping("/submissions")
    public List<DevFileSubmissionResponseDTO> getMySubmissions(@RequestHeader("username") String username) {
        User dev = userRepo.findByUsername(username).orElseThrow();
        return submissionRepo.findByDeveloper(dev).stream().map(sub -> {
            DevFileSubmissionResponseDTO dto = new DevFileSubmissionResponseDTO();
            dto.submissionID = sub.getSubmissionID();
            dto.developerUsername = dev.getUsername();
            dto.fileUrl = sub.getFileUrl();
            dto.description = sub.getDescription();
            dto.status = sub.getStatus();
            dto.adminNote = sub.getAdminNote();
            dto.submittedAt = sub.getSubmittedAt();
            return dto;
        }).collect(Collectors.toList());
    }

    // Thêm submission mới
    @PostMapping("/submissions")
    public ResponseEntity<?> createSubmission(@RequestBody DevFileSubmissionDTO req, @RequestHeader("username") String username) {
        User dev = userRepo.findByUsername(username).orElseThrow();
        GameFileSubmission sub = new GameFileSubmission();
        sub.setDeveloper(dev);
        sub.setFileUrl(req.getFileUrl());
        sub.setDescription(req.getDescription());
        sub.setStatus("pending");
        sub.setAdminNote("");
        sub.setSubmittedAt(LocalDateTime.now());
        submissionRepo.save(sub);
        return ResponseEntity.ok("OK");
    }

    // Sửa submission (chỉ cho phép sửa nếu là chủ sở hữu và chưa được duyệt)
    @PutMapping("/submissions/{id}")
    public ResponseEntity<?> updateSubmission(@PathVariable Integer id, @RequestBody DevFileSubmissionDTO req, @RequestHeader("username") String username) {
        User dev = userRepo.findByUsername(username).orElseThrow();
        GameFileSubmission sub = submissionRepo.findById(id).orElseThrow();
        if (!sub.getDeveloper().getUserID().equals(dev.getUserID())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền sửa submission này!");
        }
        if (sub.getStatus() != null && sub.getStatus().equalsIgnoreCase("approved")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể sửa submission đã được duyệt!");
        }
        sub.setFileUrl(req.getFileUrl());
        sub.setDescription(req.getDescription());
        submissionRepo.save(sub);
        return ResponseEntity.ok("OK");
    }

    // Xóa submission (chỉ cho phép xóa nếu là chủ sở hữu và chưa được duyệt)
    @DeleteMapping("/submissions/{id}")
    public ResponseEntity<?> deleteSubmission(@PathVariable Integer id, @RequestHeader("username") String username) {
        User dev = userRepo.findByUsername(username).orElseThrow();
        GameFileSubmission sub = submissionRepo.findById(id).orElseThrow();
        if (!sub.getDeveloper().getUserID().equals(dev.getUserID())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Không có quyền xóa submission này!");
        }
        if (sub.getStatus() != null && sub.getStatus().equalsIgnoreCase("approved")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể xóa submission đã được duyệt!");
        }
        submissionRepo.delete(sub);
        return ResponseEntity.ok("Đã xóa submission");
    }

    // Upload file (trả về url)
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
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
} 