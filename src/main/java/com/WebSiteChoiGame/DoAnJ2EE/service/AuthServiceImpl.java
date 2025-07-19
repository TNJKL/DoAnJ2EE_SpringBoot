package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.dto.*;
import com.WebSiteChoiGame.DoAnJ2EE.dto.AuthResponse;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Role;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.RoleRepository;
import com.WebSiteChoiGame.DoAnJ2EE.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.util.Collections;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public User register(RegisterRequest request) {
        // Kiểm tra trùng username/email
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        // Luôn gán role là 'user' cho tài khoản tự đăng ký
        Role role = roleRepository.findByRoleName("user")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role user!"));
        // Hash password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(request.getPassword());
        // Tạo user mới
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(hashedPassword);
        user.setRole(role);
        user.setCreatedAt(java.time.LocalDateTime.now());
        // Lưu user
        return userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request) {
        // Tìm user theo username hoặc email
        Optional<User> userOpt = userRepository.findByUsername(request.getUsernameOrEmail());
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(request.getUsernameOrEmail());
        }
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Tài khoản không tồn tại!");
        }
        User user = userOpt.get();
        // Kiểm tra password
        if (!encoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu không đúng!");
        }
        // Sinh JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().getRoleName());
        return token;
    }

    @Override
    public AuthResponse googleLogin(String googleToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("")) //them client ID của bạn vào đây
                    .build();
            GoogleIdToken idToken = verifier.verify(googleToken);
            if (idToken == null) {
                throw new RuntimeException("Google token không hợp lệ!");
            }
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleId = payload.getSubject();
            String name = (String) payload.get("name");
            // Kiểm tra user đã tồn tại chưa
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                // Lấy role user mặc định
                Role role = roleRepository.findByRoleName("user").orElseThrow(() -> new RuntimeException("Không tìm thấy role user!"));
                user = new User();
                user.setUsername(name != null ? name : email);
                user.setEmail(email);
                user.setGoogleID(googleId);
                user.setRole(role);
                user.setCreatedAt(java.time.LocalDateTime.now());
                user = userRepository.save(user);
            }
            // Sinh JWT token
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().getRoleName());
            return new AuthResponse(token, user.getRole().getRoleName(), user.getUsername(), user.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("Xác thực Google thất bại: " + e.getMessage());
        }
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        // TODO: Xử lý quên mật khẩu
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        // TODO: Xử lý reset mật khẩu
    }

    public Optional<User> getUserByUsernameOrEmail(String usernameOrEmail) {
        Optional<User> userOpt = userRepository.findByUsername(usernameOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(usernameOrEmail);
        }
        return userOpt;
    }
} 