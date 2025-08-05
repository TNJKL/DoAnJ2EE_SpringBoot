package com.WebSiteChoiGame.DoAnJ2EE.interceptor;

import com.WebSiteChoiGame.DoAnJ2EE.entity.GameSession;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Game;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameSessionRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class GameScoreInterceptor implements HandlerInterceptor {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Chỉ xử lý cho API saveHighScore
        if (request.getMethod().equals("POST") && request.getRequestURI().matches("/api/user/games/\\d+/score")) {
            System.out.println("=== GAME SCORE INTERCEPTOR ===");
            System.out.println("Intercepting score save request");
            
            // Lưu request body để xử lý sau
            request.setAttribute("intercepted", true);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Chỉ xử lý sau khi API saveHighScore thành công
        if (request.getAttribute("intercepted") != null && response.getStatus() == 200) {
            try {
                // Lấy thông tin từ request
                String uri = request.getRequestURI();
                String[] pathParts = uri.split("/");
                Integer gameId = Integer.parseInt(pathParts[pathParts.length - 2]);
                
                // Lấy request body (cần implement cách lấy body)
                // Tạm thời sẽ xử lý trong controller
                System.out.println("Score saved successfully, will create GameSession in controller");
            } catch (Exception e) {
                System.err.println("Error in GameScoreInterceptor: " + e.getMessage());
            }
        }
    }
} 