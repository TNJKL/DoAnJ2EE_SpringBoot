package com.WebSiteChoiGame.DoAnJ2EE.controller;

import com.WebSiteChoiGame.DoAnJ2EE.entity.Game;
import com.WebSiteChoiGame.DoAnJ2EE.entity.GameGenre;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.entity.ScoreBoard;
import com.WebSiteChoiGame.DoAnJ2EE.dto.UserGameDTO;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameGenreRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.ScoreBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameSessionRepository;
import com.WebSiteChoiGame.DoAnJ2EE.entity.GameSession;
import com.WebSiteChoiGame.DoAnJ2EE.service.ScoreBoardService;
import com.WebSiteChoiGame.DoAnJ2EE.service.UserCoinsService;

@RestController
@RequestMapping("/api/user")
public class UserMainController {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameGenreRepository gameGenreRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScoreBoardRepository scoreBoardRepository;

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private ScoreBoardService scoreBoardService;

    @Autowired
    private UserCoinsService userCoinsService;

    // Constructor để debug
    public UserMainController() {
        System.out.println("UserMainController initialized");
    }

    @PostConstruct
    public void init() {
        System.out.println("ScoreBoardRepository: " + (scoreBoardRepository != null ? "INJECTED" : "NULL"));
    }

    // API lấy danh sách game (có filter tìm kiếm, thể loại, visible)
    @GetMapping("/games")
    public List<Game> getGames(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "genreId", required = false) Integer genreId,
            @RequestParam(value = "visible", required = false, defaultValue = "true") Boolean visible
    ) {
        List<Game> games = gameRepository.findAll();
        return games.stream()
                .filter(g -> (visible == null || Boolean.TRUE.equals(g.getIsVisible())) )
                .filter(g -> search == null || g.getTitle().toLowerCase().contains(search.toLowerCase()))
                .filter(g -> genreId == null || (g.getGenre() != null && genreId.equals(g.getGenre().getGenreID())))
                .collect(Collectors.toList());
    }

    // API lấy danh sách game tối ưu (sử dụng DTO để tránh infinite recursion)
    @GetMapping("/games/optimized")
    public List<UserGameDTO> getGamesOptimized(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "genreId", required = false) Integer genreId,
            @RequestParam(value = "visible", required = false, defaultValue = "true") Boolean visible
    ) {
        List<Game> games = gameRepository.findAll();
        return games.stream()
                .filter(g -> (visible == null || Boolean.TRUE.equals(g.getIsVisible())) )
                .filter(g -> search == null || g.getTitle().toLowerCase().contains(search.toLowerCase()))
                .filter(g -> genreId == null || (g.getGenre() != null && genreId.equals(g.getGenre().getGenreID())))
                .map(UserGameDTO::new)
                .collect(Collectors.toList());
    }

    // API lấy danh sách thể loại game
    @GetMapping("/game-genres")
    public List<GameGenre> getAllGameGenres() {
        return gameGenreRepository.findAll();
    }

    // Lấy leaderboard theo điểm cao
    @GetMapping("/leaderboard")
    public List<Map<String, Object>> getLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        return scoreBoardService.getTopPlayers(limit);
    }

    // Lấy leaderboard theo coin cao nhất
    @GetMapping("/coin-leaderboard")
    public List<Map<String, Object>> getCoinLeaderboard(@RequestParam(defaultValue = "10") int limit) {
        return userCoinsService.getTopCoinUsers(limit);
    }

    // Lấy leaderboard theo game cụ thể
    @GetMapping("/games/{gameId}/leaderboard")
    public List<Map<String, Object>> getGameLeaderboard(@PathVariable Integer gameId, @RequestParam(defaultValue = "10") int limit) {
        return scoreBoardService.getTopPlayersByGame(gameId, limit);
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable Integer gameId) {
        try {
            System.out.println("Fetching game with ID: " + gameId);
            Game game = gameRepository.findById(gameId).orElse(null);
            System.out.println("Game found: " + (game != null ? game.getTitle() : "null"));
            
            if (game == null) {
                System.out.println("Game not found");
                return ResponseEntity.notFound().build();
            }
            
            if (!Boolean.TRUE.equals(game.getIsVisible())) {
                System.out.println("Game not visible");
                return ResponseEntity.notFound().build();
            }
            
            if (!Boolean.TRUE.equals(game.getIsApproved())) {
                System.out.println("Game not approved");
                return ResponseEntity.notFound().build();
            }
            
            System.out.println("Returning game: " + game.getTitle());
            return ResponseEntity.ok(game);
        } catch (Exception e) {
            System.err.println("Error fetching game: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/games/{gameId}/optimized")
    public ResponseEntity<UserGameDTO> getGameByIdOptimized(@PathVariable Integer gameId) {
        try {
            System.out.println("Fetching game with ID: " + gameId);
            Game game = gameRepository.findById(gameId).orElse(null);
            System.out.println("Game found: " + (game != null ? game.getTitle() : "null"));
            
            if (game == null) {
                System.out.println("Game not found");
                return ResponseEntity.notFound().build();
            }
            
            if (!Boolean.TRUE.equals(game.getIsVisible())) {
                System.out.println("Game not visible");
                return ResponseEntity.notFound().build();
            }
            
            if (!Boolean.TRUE.equals(game.getIsApproved())) {
                System.out.println("Game not approved");
                return ResponseEntity.notFound().build();
            }
            
            System.out.println("Returning optimized game: " + game.getTitle());
            return ResponseEntity.ok(new UserGameDTO(game));
        } catch (Exception e) {
            System.err.println("Error fetching game: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // API lưu điểm cao nhất
    @PostMapping("/games/{gameId}/score")
    public synchronized ResponseEntity<?> saveHighScore(
            @PathVariable Integer gameId,
            @RequestBody Map<String, Object> request
    ) {
        try {
            Integer score = (Integer) request.get("score");
            String username = (String) request.get("username");
            
            System.out.println("=== SAVE SCORE REQUEST ===");
            System.out.println("Game ID: " + gameId);
            System.out.println("Username: " + username);
            System.out.println("Score to save: " + score);
            
            if (score == null || username == null) {
                System.out.println("ERROR: Missing score or username");
                return ResponseEntity.badRequest().body("Missing score or username");
            }

            // Tìm user
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                System.out.println("ERROR: User not found: " + username);
                return ResponseEntity.badRequest().body("User not found");
            }
            System.out.println("User found: " + user.getUsername());

            // Tìm game
            Game game = gameRepository.findById(gameId).orElse(null);
            if (game == null) {
                System.out.println("ERROR: Game not found: " + gameId);
                return ResponseEntity.badRequest().body("Game not found");
            }
            System.out.println("Game found: " + game.getTitle());

            // Tìm tất cả ScoreBoard cho user và game này (xử lý duplicate)
            List<ScoreBoard> existingScores = scoreBoardRepository.findAllByUserAndGame(user, game);
            ScoreBoard scoreBoard = null;
            Integer currentHighScore = 0;
            
            if (!existingScores.isEmpty()) {
                // Nếu có nhiều bản ghi, lấy bản ghi có điểm cao nhất
                scoreBoard = existingScores.stream()
                    .max((s1, s2) -> Integer.compare(
                        s1.getHighScore() != null ? s1.getHighScore() : 0,
                        s2.getHighScore() != null ? s2.getHighScore() : 0
                    ))
                    .orElse(null);
                
                // Xóa các bản ghi duplicate khác
                if (existingScores.size() > 1 && scoreBoard != null) {
                    System.out.println("Found " + existingScores.size() + " duplicate records, keeping the best one");
                    final ScoreBoard finalScoreBoard = scoreBoard;
                    existingScores.stream()
                        .filter(sb -> !sb.getScoreID().equals(finalScoreBoard.getScoreID()))
                        .forEach(sb -> {
                            System.out.println("Deleting duplicate record: " + sb.getScoreID());
                            scoreBoardRepository.delete(sb);
                        });
                }
                
                currentHighScore = scoreBoard != null ? scoreBoard.getHighScore() : 0;
            }
            
            System.out.println("Current high score in DB: " + currentHighScore);
            
            if (scoreBoard == null) {
                System.out.println("Creating new ScoreBoard entry");
                try {
                    scoreBoard = new ScoreBoard();
                    scoreBoard.setUser(user);
                    scoreBoard.setGame(game);
                    scoreBoard.setHighScore(score);
                    scoreBoard.setLastPlayed(java.time.LocalDateTime.now());
                    scoreBoardRepository.save(scoreBoard);
                    System.out.println("High score saved successfully");
                    return ResponseEntity.ok(Map.of("message", "High score updated", "newHighScore", score));
                } catch (Exception e) {
                    System.out.println("Error creating new record, checking for existing records: " + e.getMessage());
                    // Nếu tạo record mới thất bại (có thể do unique constraint), tìm record hiện có
                    List<ScoreBoard> checkExistingScores = scoreBoardRepository.findAllByUserAndGame(user, game);
                    if (!checkExistingScores.isEmpty()) {
                        scoreBoard = checkExistingScores.stream()
                            .max((s1, s2) -> Integer.compare(
                                s1.getHighScore() != null ? s1.getHighScore() : 0,
                                s2.getHighScore() != null ? s2.getHighScore() : 0
                            ))
                            .orElse(null);
                        
                        if (scoreBoard != null && (scoreBoard.getHighScore() == null || score > scoreBoard.getHighScore())) {
                            scoreBoard.setHighScore(score);
                            scoreBoard.setLastPlayed(java.time.LocalDateTime.now());
                            scoreBoardRepository.save(scoreBoard);
                            System.out.println("Updated existing record successfully");
                            return ResponseEntity.ok(Map.of("message", "High score updated", "newHighScore", score));
                        }
                    }
                    throw e; // Re-throw nếu không xử lý được
                }
            }

            // Cập nhật điểm cao nhất nếu điểm mới cao hơn (cho record đã tồn tại)
            if (scoreBoard.getHighScore() == null || score > scoreBoard.getHighScore()) {
                System.out.println("UPDATING HIGH SCORE: " + currentHighScore + " -> " + score);
                scoreBoard.setHighScore(score);
                scoreBoard.setLastPlayed(java.time.LocalDateTime.now());
                scoreBoardRepository.save(scoreBoard);
                System.out.println("High score updated successfully");
                return ResponseEntity.ok(Map.of("message", "High score updated", "newHighScore", score));
            } else {
                System.out.println("Score not higher than current high score. Keeping: " + currentHighScore);
                return ResponseEntity.ok(Map.of("message", "Score not higher than current high score", "currentHighScore", scoreBoard.getHighScore()));
            }
        } catch (Exception e) {
            System.err.println("ERROR saving score: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    // API lưu điểm vào GameSessions (cho challenge)
    @PostMapping("/games/{gameId}/session-score")
    public synchronized ResponseEntity<?> saveGameSessionScore(
            @PathVariable Integer gameId,
            @RequestBody Map<String, Object> request
    ) {
        try {
            Integer score = (Integer) request.get("score");
            String username = (String) request.get("username");
            
            System.out.println("=== SAVE GAME SESSION SCORE ===");
            System.out.println("Game ID: " + gameId);
            System.out.println("Username: " + username);
            System.out.println("Score to save: " + score);
            
            if (score == null || username == null) {
                System.out.println("ERROR: Missing score or username");
                return ResponseEntity.badRequest().body("Missing score or username");
            }

            // Tìm user
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                System.out.println("ERROR: User not found: " + username);
                return ResponseEntity.badRequest().body("User not found");
            }
            System.out.println("User found: " + user.getUsername());

            // Tìm game
            Game game = gameRepository.findById(gameId).orElse(null);
            if (game == null) {
                System.out.println("ERROR: Game not found: " + gameId);
                return ResponseEntity.badRequest().body("Game not found");
            }
            System.out.println("Game found: " + game.getTitle());

            // Tạo GameSession mới
            GameSession gameSession = new GameSession();
            gameSession.setUser(user);
            gameSession.setGame(game);
            gameSession.setScore(score);
            gameSession.setPlayedAt(java.time.LocalDateTime.now());
            
            gameSessionRepository.save(gameSession);
            System.out.println("Game session score saved successfully: " + score);
            
            return ResponseEntity.ok(Map.of(
                "message", "Game session score saved", 
                "score", score,
                "sessionID", gameSession.getSessionID()
            ));
            
        } catch (Exception e) {
            System.err.println("ERROR saving game session score: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    // API lấy điểm cao nhất của user cho game
    @GetMapping("/games/{gameId}/score")
    public ResponseEntity<?> getHighScore(
            @PathVariable Integer gameId,
            @RequestParam String username
    ) {
        try {
            System.out.println("Getting high score for gameId: " + gameId + ", username: " + username);
            System.out.println("ScoreBoardRepository: " + (scoreBoardRepository != null ? "AVAILABLE" : "NULL"));
            
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                System.out.println("User not found: " + username);
                return ResponseEntity.badRequest().body("User not found");
            }
            System.out.println("User found: " + user.getUsername());

            Game game = gameRepository.findById(gameId).orElse(null);
            if (game == null) {
                System.out.println("Game not found: " + gameId);
                return ResponseEntity.badRequest().body("Game not found");
            }
            System.out.println("Game found: " + game.getTitle());

            ScoreBoard scoreBoard = scoreBoardRepository.findByUserAndGame(user, game).orElse(null);
            Integer highScore = scoreBoard != null ? scoreBoard.getHighScore() : 0;
            System.out.println("High score: " + highScore);

            return ResponseEntity.ok(Map.of("highScore", highScore));
        } catch (Exception e) {
            System.err.println("Error getting score: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(Map.of("highScore", 0));
        }
    }

    // DTO trả về cho leaderboard
    public static class UserLeaderboardDTO {
        public Integer userID;
        public String username;
        public String email;
        public Integer highScore;
        public UserLeaderboardDTO(User user, Integer highScore) {
            this.userID = user.getUserID();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.highScore = highScore;
        }
    }
} 