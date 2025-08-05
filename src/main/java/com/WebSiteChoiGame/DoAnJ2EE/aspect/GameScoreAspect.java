package com.WebSiteChoiGame.DoAnJ2EE.aspect;

import com.WebSiteChoiGame.DoAnJ2EE.entity.GameSession;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Game;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameSessionRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Aspect
@Component
public class GameScoreAspect {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @AfterReturning(
        pointcut = "execution(* com.WebSiteChoiGame.DoAnJ2EE.controller.UserMainController.saveHighScore(..))",
        returning = "result"
    )
    public void afterSaveHighScore(JoinPoint joinPoint, Object result) {
        try {
            System.out.println("=== GAME SCORE ASPECT ===");
            System.out.println("Intercepting saveHighScore success");
            
            // Lấy tham số từ method call
            Object[] args = joinPoint.getArgs();
            if (args.length >= 2) {
                Integer gameId = (Integer) args[0];
                Map<String, Object> request = (Map<String, Object>) args[1];
                
                Integer score = (Integer) request.get("score");
                String username = (String) request.get("username");
                
                System.out.println("Game ID: " + gameId);
                System.out.println("Username: " + username);
                System.out.println("Score: " + score);
                
                if (score != null && username != null) {
                    // Tìm user
                    User user = userRepository.findByUsername(username).orElse(null);
                    if (user == null) {
                        System.out.println("User not found: " + username);
                        return;
                    }
                    
                    // Tìm game
                    Game game = gameRepository.findById(gameId).orElse(null);
                    if (game == null) {
                        System.out.println("Game not found: " + gameId);
                        return;
                    }
                    
                    // Tạo GameSession mới
                    GameSession gameSession = new GameSession();
                    gameSession.setUser(user);
                    gameSession.setGame(game);
                    gameSession.setScore(score);
                    gameSession.setPlayedAt(LocalDateTime.now());
                    
                    gameSessionRepository.save(gameSession);
                    System.out.println("GameSession created successfully: " + gameSession.getSessionID());
                }
            }
        } catch (Exception e) {
            System.err.println("Error in GameScoreAspect: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 