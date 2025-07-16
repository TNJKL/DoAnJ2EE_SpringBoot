package com.WebSiteChoiGame.DoAnJ2EE.controller;

import com.WebSiteChoiGame.DoAnJ2EE.dto.*;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            AuthResponse response = new AuthResponse(null, user.getRole().getRoleName(), user.getUsername(), user.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            // Lấy user để trả về thông tin
            User user = null;
            if (authService instanceof com.WebSiteChoiGame.DoAnJ2EE.service.AuthServiceImpl impl) {
                Optional<User> userOpt = impl.getUserByUsernameOrEmail(request.getUsernameOrEmail());
                if (userOpt.isPresent()) {
                    user = userOpt.get();
                }
            }
            if (user == null) {
                return ResponseEntity.badRequest().body("Tài khoản không tồn tại!");
            }
            AuthResponse response = new AuthResponse(token, user.getRole().getRoleName(), user.getUsername(), user.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        // TODO: Xử lý quên mật khẩu
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        // TODO: Xử lý reset mật khẩu
        return ResponseEntity.ok().build();
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody String googleToken) {
        // TODO: Xác thực Google token, tạo user mới nếu chưa có, trả về AuthResponse (token, role, username, email)
        return ResponseEntity.status(501).body("Chức năng Google Login sẽ được tích hợp khi có API key!");
    }
} 