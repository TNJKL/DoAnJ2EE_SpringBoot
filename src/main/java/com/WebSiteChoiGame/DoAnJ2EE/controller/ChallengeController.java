package com.WebSiteChoiGame.DoAnJ2EE.controller;

import com.WebSiteChoiGame.DoAnJ2EE.dto.ChallengeDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.ChallengeBetDTO;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Challenge;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameSessionRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.ChallengeRepository;
import com.WebSiteChoiGame.DoAnJ2EE.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/challenges")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ChallengeController {
    
    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    // Tạo thách đấu mới
    @PostMapping
    public ChallengeDTO createChallenge(@RequestBody Map<String, Object> request) {
        System.out.println("Received request: " + request);
        
        String challengerUsername = (String) request.get("challengerUsername");
        String opponentUsername = (String) request.get("opponentUsername");
        Integer gameID = (Integer) request.get("gameID");
        Integer betAmount = (Integer) request.get("betAmount");
        
        System.out.println("Parsed values: challengerUsername=" + challengerUsername + 
                         ", opponentUsername=" + opponentUsername + 
                         ", gameID=" + gameID + 
                         ", betAmount=" + betAmount);
        
        // Validation
        if (challengerUsername == null || challengerUsername.trim().isEmpty()) {
            throw new RuntimeException("ChallengerUsername không được null!");
        }
        if (opponentUsername == null || opponentUsername.trim().isEmpty()) {
            throw new RuntimeException("OpponentUsername không được null!");
        }
        if (gameID == null) {
            throw new RuntimeException("GameID không được null!");
        }
        if (betAmount == null || betAmount <= 0) {
            throw new RuntimeException("BetAmount phải lớn hơn 0!");
        }
        
        return challengeService.createChallengeByUsername(challengerUsername, opponentUsername, gameID, betAmount);
    }

    // Chấp nhận thách đấu
    @PostMapping("/{challengeID}/accept")
    public ChallengeDTO acceptChallenge(@PathVariable Integer challengeID, @RequestBody Map<String, Object> request) {
        String opponentUsername = (String) request.get("opponentUsername");
        return challengeService.acceptChallengeByUsername(challengeID, opponentUsername);
    }

    // Từ chối thách đấu
    @PostMapping("/{challengeID}/decline")
    public ChallengeDTO declineChallenge(@PathVariable Integer challengeID, @RequestBody Map<String, Object> request) {
        String opponentUsername = (String) request.get("opponentUsername");
        return challengeService.declineChallengeByUsername(challengeID, opponentUsername);
    }

    // Bắt đầu thách đấu
    @PostMapping("/{challengeID}/start")
    public ChallengeDTO startChallenge(@PathVariable Integer challengeID) {
        return challengeService.startChallenge(challengeID);
    }

    // Kết thúc thách đấu (timeout)
    @PostMapping("/{challengeID}/finish")
    public ChallengeDTO finishChallenge(@PathVariable Integer challengeID, @RequestBody Map<String, Object> request) {
        Integer challengerScore = (Integer) request.get("challengerScore");
        Integer opponentScore = (Integer) request.get("opponentScore");
        return challengeService.finishChallenge(challengeID, challengerScore, opponentScore);
    }

    // Kết thúc thách đấu khi game thực sự kết thúc
    @PostMapping("/{challengeID}/finish-game")
    public ChallengeDTO finishChallengeByGameEnd(@PathVariable Integer challengeID, @RequestBody Map<String, Object> request) {
        Integer challengerScore = (Integer) request.get("challengerScore");
        Integer opponentScore = (Integer) request.get("opponentScore");
        return challengeService.finishChallengeByGameEnd(challengeID, challengerScore, opponentScore);
    }

    // Lấy thông tin thách đấu
    @GetMapping("/{challengeID}")
    public ChallengeDTO getChallenge(@PathVariable Integer challengeID) {
        return challengeService.getChallengeById(challengeID);
    }

    // Lấy danh sách thách đấu đang chờ
    @GetMapping("/pending")
    public List<ChallengeDTO> getPendingChallenges() {
        return challengeService.getPendingChallenges();
    }

    // Lấy danh sách thách đấu đang diễn ra
    @GetMapping("/active")
    public List<ChallengeDTO> getActiveChallenges() {
        return challengeService.getActiveChallenges();
    }

    // Lấy danh sách thách đấu đã hoàn thành
    @GetMapping("/finished")
    public List<ChallengeDTO> getFinishedChallenges() {
        return challengeService.getFinishedChallenges();
    }

    // Lấy thách đấu của user
    @GetMapping("/user/{userID}")
    public List<ChallengeDTO> getUserChallenges(@PathVariable Integer userID) {
        return challengeService.getUserChallenges(userID);
    }

    // Lấy tất cả thách đấu
    @GetMapping
    public List<ChallengeDTO> getAllChallenges() {
        return challengeService.getAllChallenges();
    }

    // Lấy thách đấu đang chờ theo username
    @GetMapping("/pending/{username}")
    public List<ChallengeDTO> getPendingChallengesByUsername(@PathVariable String username) {
        return challengeService.getPendingChallengesByUsername(username);
    }

    // Lấy thách đấu của user theo username
    @GetMapping("/user-username/{username}")
    public List<ChallengeDTO> getUserChallengesByUsername(@PathVariable String username) {
        return challengeService.getUserChallengesByUsername(username);
    }

    // Lấy danh sách thách đấu có thể đặt cược
    @GetMapping("/betting")
    public List<ChallengeDTO> getBettingChallenges() {
        return challengeService.getBettingChallenges();
    }

    // Cập nhật thách đấu hết hạn
    @PostMapping("/update-expired")
    public void updateExpiredChallenges() {
        challengeService.updateExpiredChallenges();
    }

    // Lấy danh sách tất cả users
    @GetMapping("/users-list")
    public List<Map<String, Object>> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("userID", user.getUserID());
                    userMap.put("username", user.getUsername());
                    userMap.put("email", user.getEmail());
                    return userMap;
                })
                .collect(Collectors.toList());
    }

    // Lấy danh sách tất cả games
    @GetMapping("/games-list")
    public List<Map<String, Object>> getAllGames() {
        return gameRepository.findAll().stream()
                .map(game -> {
                    Map<String, Object> gameMap = new HashMap<>();
                    gameMap.put("gameID", game.getGameID());
                    gameMap.put("title", game.getTitle());
                    gameMap.put("description", game.getDescription());
                    return gameMap;
                })
                .collect(Collectors.toList());
    }

    // Đặt cược
    @PostMapping("/{challengeID}/bet")
    public ChallengeBetDTO placeBetByUsername(@PathVariable Integer challengeID, @RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            String betOnUsername = (String) request.get("betOnUsername");
            Integer betAmount = (Integer) request.get("betAmount");
            // Validation
            if (username == null || username.trim().isEmpty()) {
                throw new RuntimeException("username không được null!");
            }
            if (betOnUsername == null || betOnUsername.trim().isEmpty()) {
                throw new RuntimeException("betOnUsername không được null!");
            }
            if (betAmount == null || betAmount <= 0) {
                throw new RuntimeException("betAmount phải lớn hơn 0!");
            }
            return challengeService.placeBetByUsername(challengeID, username, betOnUsername, betAmount);
        } catch (Exception e) {
            System.err.println("Error in placeBetByUsername: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Lấy danh sách cược của một thách đấu
    @GetMapping("/{challengeID}/bets")
    public List<ChallengeBetDTO> getChallengeBets(@PathVariable Integer challengeID) {
        return challengeService.getChallengeBets(challengeID);
    }

    // Lấy danh sách cược của user
    @GetMapping("/user/{userID}/bets")
    public List<ChallengeBetDTO> getUserBets(@PathVariable Integer userID) {
        return challengeService.getUserBets(userID);
    }

    // Kiểm tra user có thể đặt cược không
    @GetMapping("/{challengeID}/can-bet/{username}")
    public Map<String, Boolean> canUserBet(@PathVariable Integer challengeID, @PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        boolean canBet = challengeService.canUserBet(challengeID, user.getUserID());
        return Map.of("canBet", canBet);
    }

    // Kiểm tra trạng thái thách đấu
    @GetMapping("/{challengeID}/status")
    public Map<String, Boolean> getChallengeStatus(@PathVariable Integer challengeID) {
        boolean isActive = challengeService.isChallengeActive(challengeID);
        boolean isFinished = challengeService.isChallengeFinished(challengeID);
        return Map.of(
            "isActive", isActive,
            "isFinished", isFinished
        );
    }

    // Lấy điểm số từ GameSessions cho challenge
    @GetMapping("/{challengeID}/scores")
    public Map<String, Object> getChallengeScores(@PathVariable Integer challengeID) {
        Challenge challenge = challengeRepository.findById(challengeID)
                .orElseThrow(() -> new RuntimeException("Challenge không tồn tại!"));
        
        // Nếu challenge chưa kết thúc, sử dụng thời gian hiện tại
        LocalDateTime endTime = challenge.getEndTime() != null ? challenge.getEndTime() : LocalDateTime.now();
        
        // Lấy điểm số từ GameSessions trong khoảng thời gian challenge
        Integer challengerScore = gameSessionRepository.findScoreByUserAndGameAndTimeRange(
            challenge.getChallenger().getUserID(), 
            challenge.getGame().getGameID(),
            challenge.getStartTime(),
            endTime
        );
        
        Integer opponentScore = gameSessionRepository.findScoreByUserAndGameAndTimeRange(
            challenge.getOpponent().getUserID(), 
            challenge.getGame().getGameID(),
            challenge.getStartTime(),
            endTime
        );
        
        return Map.of(
            "challengerScore", challengerScore != null ? challengerScore : 0,
            "opponentScore", opponentScore != null ? opponentScore : 0
        );
    }

    // Kiểm tra và tự động kết thúc challenge khi cả 2 người đã chơi xong
    @PostMapping("/{challengeID}/check-completion")
    public Map<String, Object> checkChallengeCompletion(@PathVariable Integer challengeID) {
        Challenge challenge = challengeRepository.findById(challengeID)
                .orElseThrow(() -> new RuntimeException("Challenge không tồn tại!"));
        
        // Kiểm tra xem challenge đã được finish chưa
        if (challenge.getChallengerScore() != null && challenge.getOpponentScore() != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("completed", true);
            response.put("message", "Challenge đã được kết thúc trước đó");
            response.put("challengerPlayed", true);
            response.put("opponentPlayed", true);
            response.put("challengerScore", challenge.getChallengerScore());
            response.put("opponentScore", challenge.getOpponentScore());
            
            // Luôn trả về thông tin winner, kể cả khi challenge đã finished
            if (challenge.getWinner() != null) {
                response.put("winner", challenge.getWinner().getUsername());
                response.put("winnerID", challenge.getWinner().getUserID());
            } else {
                response.put("winner", null);
                response.put("winnerID", null);
            }
            
            return response;
        }
        
        // Nếu challenge chưa kết thúc, sử dụng thời gian hiện tại
        LocalDateTime endTime = challenge.getEndTime() != null ? challenge.getEndTime() : LocalDateTime.now();
        
        // Lấy điểm số từ GameSessions trong khoảng thời gian challenge
        Integer challengerScore = gameSessionRepository.findScoreByUserAndGameAndTimeRange(
            challenge.getChallenger().getUserID(), 
            challenge.getGame().getGameID(),
            challenge.getStartTime(),
            endTime
        );
        
        Integer opponentScore = gameSessionRepository.findScoreByUserAndGameAndTimeRange(
            challenge.getOpponent().getUserID(), 
            challenge.getGame().getGameID(),
            challenge.getStartTime(),
            endTime
        );
        
        // Kiểm tra xem cả 2 người đã chơi chưa
        boolean challengerPlayed = challengerScore != null && challengerScore > 0;
        boolean opponentPlayed = opponentScore != null && opponentScore > 0;
        
        Map<String, Object> response = new HashMap<>();
        response.put("challengerPlayed", challengerPlayed);
        response.put("opponentPlayed", opponentPlayed);
        response.put("challengerScore", challengerScore != null ? challengerScore : 0);
        response.put("opponentScore", opponentScore != null ? opponentScore : 0);
        
        // Nếu cả 2 đã chơi, tự động kết thúc challenge
        if (challengerPlayed && opponentPlayed) {
            try {
                ChallengeDTO result = challengeService.finishChallengeByGameEnd(challengeID, challengerScore, opponentScore);
                response.put("completed", true);
                response.put("message", "Challenge đã được kết thúc tự động!");
                response.put("winner", result.getWinnerUsername());
                response.put("winnerID", result.getWinnerID());
                return response;
            } catch (Exception e) {
                response.put("completed", false);
                response.put("error", e.getMessage());
                return response;
            }
        } else {
            response.put("completed", false);
            response.put("message", "Chưa đủ người chơi để kết thúc challenge");
        }
        
        return response;
    }
} 