package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.dto.*;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;

public interface AuthService {
    User register(RegisterRequest request);
    String login(LoginRequest request);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    AuthResponse googleLogin(String googleToken);
} 