package com.WebSiteChoiGame.DoAnJ2EE.controller;

import com.WebSiteChoiGame.DoAnJ2EE.entity.Game;
import com.WebSiteChoiGame.DoAnJ2EE.entity.GameGenre;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.entity.ScoreBoard;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameGenreRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.ScoreBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserMainController {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameGenreRepository gameGenreRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired(required = false)
    private ScoreBoardRepository scoreBoardRepository;

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

    // API lấy danh sách thể loại game
    @GetMapping("/game-genres")
    public List<GameGenre> getAllGameGenres() {
        return gameGenreRepository.findAll();
    }

    // API lấy leaderboard (top N user có điểm cao nhất)
    @GetMapping("/leaderboard")
    public List<UserLeaderboardDTO> getLeaderboard(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        // Lấy tất cả ScoreBoard, tổng hợp điểm cao nhất của mỗi user
        List<ScoreBoard> boards = scoreBoardRepository.findAll();
        // Map userID -> điểm cao nhất
        java.util.Map<Integer, Integer> userScoreMap = new java.util.HashMap<>();
        java.util.Map<Integer, User> userMap = new java.util.HashMap<>();
        for (ScoreBoard sb : boards) {
            int uid = sb.getUser().getUserID();
            int score = sb.getHighScore() != null ? sb.getHighScore() : 0;
            if (!userScoreMap.containsKey(uid) || score > userScoreMap.get(uid)) {
                userScoreMap.put(uid, score);
                userMap.put(uid, sb.getUser());
            }
        }
        // Sắp xếp theo điểm giảm dần và lấy top N
        return userScoreMap.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(limit)
                .map(e -> new UserLeaderboardDTO(userMap.get(e.getKey()), e.getValue()))
                .collect(java.util.stream.Collectors.toList());
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