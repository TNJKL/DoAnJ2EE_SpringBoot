package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.dto.ChallengeDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.ChallengeBetDTO;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Challenge;
import com.WebSiteChoiGame.DoAnJ2EE.entity.ChallengeBet;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Game;
import com.WebSiteChoiGame.DoAnJ2EE.entity.UserCoins;
import com.WebSiteChoiGame.DoAnJ2EE.repository.ChallengeRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.ChallengeBetRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.GameRepository;
import com.WebSiteChoiGame.DoAnJ2EE.repository.UserCoinsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeServiceImpl implements ChallengeService {
    
    @Autowired
    private ChallengeRepository challengeRepository;
    
    @Autowired
    private ChallengeBetRepository challengeBetRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private UserCoinsRepository userCoinsRepository;
    
    @Autowired
    private CoinService coinService;

    @Override
    @Transactional
    public ChallengeDTO createChallenge(Integer challengerID, Integer opponentID, Integer gameID, Integer betAmount) {
        // Kiểm tra user và game tồn tại
        User challenger = userRepository.findById(challengerID)
                .orElseThrow(() -> new RuntimeException("Challenger không tồn tại!"));
        User opponent = userRepository.findById(opponentID)
                .orElseThrow(() -> new RuntimeException("Opponent không tồn tại!"));
        Game game = gameRepository.findById(gameID)
                .orElseThrow(() -> new RuntimeException("Game không tồn tại!"));
        
        // Kiểm tra challenger có đủ coin không
        UserCoins challengerCoins = userCoinsRepository.findByUser_UserID(challengerID)
                .orElseThrow(() -> new RuntimeException("Challenger chưa có coin!"));
        
        if (challengerCoins.getCoinAmount() < betAmount) {
            throw new RuntimeException("Không đủ coin để thách đấu!");
        }
        
        // Trừ coin của challenger
        challengerCoins.setCoinAmount(challengerCoins.getCoinAmount() - betAmount);
        userCoinsRepository.save(challengerCoins);
        
        // Tạo transaction record
        coinService.createTransaction(challengerID, "bet", -betAmount, 
            "Đặt cược thách đấu với " + opponent.getUsername());
        
        // Tạo challenge
        Challenge challenge = new Challenge(challenger, opponent, game, betAmount);
        challenge = challengeRepository.save(challenge);
        
        return convertToDTO(challenge);
    }

    @Override
    @Transactional
    public ChallengeDTO createChallengeByUsername(String challengerUsername, String opponentUsername, Integer gameID, Integer betAmount) {
        // Kiểm tra user và game tồn tại
        User challenger = userRepository.findByUsername(challengerUsername)
                .orElseThrow(() -> new RuntimeException("Challenger không tồn tại!"));
        User opponent = userRepository.findByUsername(opponentUsername)
                .orElseThrow(() -> new RuntimeException("Opponent không tồn tại!"));
        Game game = gameRepository.findById(gameID)
                .orElseThrow(() -> new RuntimeException("Game không tồn tại!"));
        
        // Kiểm tra challenger có đủ coin không
        UserCoins challengerCoins = userCoinsRepository.findByUser_UserID(challenger.getUserID())
                .orElseThrow(() -> new RuntimeException("Challenger chưa có coin!"));
        
        if (challengerCoins.getCoinAmount() < betAmount) {
            throw new RuntimeException("Không đủ coin để thách đấu!");
        }
        
        // Trừ coin của challenger
        challengerCoins.setCoinAmount(challengerCoins.getCoinAmount() - betAmount);
        userCoinsRepository.save(challengerCoins);
        
        // Tạo transaction record
        coinService.createTransaction(challenger.getUserID(), "bet", -betAmount, 
            "Đặt cược thách đấu với " + opponent.getUsername());
        
        // Tạo challenge
        Challenge challenge = new Challenge(challenger, opponent, game, betAmount);
        challenge = challengeRepository.save(challenge);
        
        return convertToDTO(challenge);
    }

    @Override
    @Transactional
    public ChallengeDTO acceptChallenge(Integer challengeID, Integer opponentID) {
        Challenge challenge = challengeRepository.findById(challengeID)
                .orElseThrow(() -> new RuntimeException("Challenge không tồn tại!"));
        
        if (!challenge.getOpponent().getUserID().equals(opponentID)) {
            throw new RuntimeException("Bạn không phải là người được thách đấu!");
        }
        
        if (!"pending".equals(challenge.getStatus())) {
            throw new RuntimeException("Challenge không ở trạng thái chờ!");
        }
        
        // Kiểm tra opponent có đủ coin không
        UserCoins opponentCoins = userCoinsRepository.findByUser_UserID(opponentID)
                .orElseThrow(() -> new RuntimeException("Opponent chưa có coin!"));
        
        if (opponentCoins.getCoinAmount() < challenge.getBetAmount()) {
            throw new RuntimeException("Không đủ coin để tham gia thách đấu!");
        }
        
        // Trừ coin của opponent
        opponentCoins.setCoinAmount(opponentCoins.getCoinAmount() - challenge.getBetAmount());
        userCoinsRepository.save(opponentCoins);
        
        // Tạo transaction record
        coinService.createTransaction(opponentID, "bet", -challenge.getBetAmount(), 
            "Đặt cược thách đấu với " + challenge.getChallenger().getUsername());
        
        // Cập nhật trạng thái challenge
        challenge.setStatus("accepted");
        challenge = challengeRepository.save(challenge);
        
        return convertToDTO(challenge);
    }

    @Override
    @Transactional
    public ChallengeDTO acceptChallengeByUsername(Integer challengeID, String opponentUsername) {
        User opponent = userRepository.findByUsername(opponentUsername)
                .orElseThrow(() -> new RuntimeException("Opponent không tồn tại!"));
        
        return acceptChallenge(challengeID, opponent.getUserID());
    }

    @Override
    @Transactional
    public ChallengeDTO declineChallenge(Integer challengeID, Integer opponentID) {
        Challenge challenge = challengeRepository.findById(challengeID)
                .orElseThrow(() -> new RuntimeException("Challenge không tồn tại!"));
        
        if (!challenge.getOpponent().getUserID().equals(opponentID)) {
            throw new RuntimeException("Bạn không phải là người được thách đấu!");
        }
        
        if (!"pending".equals(challenge.getStatus())) {
            throw new RuntimeException("Challenge không ở trạng thái chờ!");
        }
        
        // Hoàn trả coin cho challenger
        UserCoins challengerCoins = userCoinsRepository.findByUser_UserID(challenge.getChallenger().getUserID())
                .orElseThrow(() -> new RuntimeException("Challenger chưa có coin!"));
        
        challengerCoins.setCoinAmount(challengerCoins.getCoinAmount() + challenge.getBetAmount());
        userCoinsRepository.save(challengerCoins);
        
        // Tạo transaction record
        coinService.createTransaction(challenge.getChallenger().getUserID(), "refund", challenge.getBetAmount(), 
            "Hoàn trả coin do " + challenge.getOpponent().getUsername() + " từ chối thách đấu");
        
        // Cập nhật trạng thái challenge
        challenge.setStatus("declined");
        challenge = challengeRepository.save(challenge);
        
        return convertToDTO(challenge);
    }

    @Override
    @Transactional
    public ChallengeDTO declineChallengeByUsername(Integer challengeID, String opponentUsername) {
        User opponent = userRepository.findByUsername(opponentUsername)
                .orElseThrow(() -> new RuntimeException("Opponent không tồn tại!"));
        
        return declineChallenge(challengeID, opponent.getUserID());
    }

    @Override
    @Transactional
    public ChallengeDTO startChallenge(Integer challengeID) {
        Challenge challenge = challengeRepository.findById(challengeID)
                .orElseThrow(() -> new RuntimeException("Challenge không tồn tại!"));
        
        if (!"accepted".equals(challenge.getStatus())) {
            throw new RuntimeException("Challenge chưa được chấp nhận!");
        }
        
        // Thiết lập thời gian bắt đầu và kết thúc (thời gian linh hoạt)
        LocalDateTime now = LocalDateTime.now();
        challenge.setStartTime(now);
        challenge.setEndTime(now.plusMinutes(10)); // 10 phút mặc định cho tất cả game
        challenge.setStatus("active");
        challenge = challengeRepository.save(challenge);
        
        return convertToDTO(challenge);
    }

    @Override
    @Transactional
    public ChallengeDTO finishChallenge(Integer challengeID, Integer challengerScore, Integer opponentScore) {
        System.out.println("=== FINISH CHALLENGE (TIMEOUT) ===");
        System.out.println("ChallengeID: " + challengeID);
        System.out.println("ChallengerScore: " + challengerScore);
        System.out.println("OpponentScore: " + opponentScore);
        
        Challenge challenge = challengeRepository.findById(challengeID)
                .orElseThrow(() -> new RuntimeException("Challenge không tồn tại!"));
        
        // Kiểm tra xem challenge đã được finish chưa
        if (challenge.getChallengerScore() != null && challenge.getOpponentScore() != null) {
            System.out.println("Challenge đã được finish trước đó!");
            return convertToDTO(challenge);
        }
        
        // Kiểm tra trạng thái challenge
        if (!"active".equals(challenge.getStatus()) && !"finished".equals(challenge.getStatus())) {
            System.out.println("Challenge không ở trạng thái active hoặc finished: " + challenge.getStatus());
            return convertToDTO(challenge);
        }
        
        // Cập nhật điểm số và trạng thái
        challenge.setChallengerScore(challengerScore);
        challenge.setOpponentScore(opponentScore);
        challenge.setStatus("finished");
        challenge.setEndTime(LocalDateTime.now());
        
        // Save ngay để lock challenge
        challenge = challengeRepository.save(challenge);
        System.out.println("Challenge status updated to finished by timeout");
        
        // Xử lý phân phối coin
        return processChallengeResult(challenge, challengerScore, opponentScore);
    }

    // Method mới để kết thúc challenge khi game thực sự kết thúc
    @Transactional
    public ChallengeDTO finishChallengeByGameEnd(Integer challengeID, Integer challengerScore, Integer opponentScore) {
        System.out.println("=== FINISH CHALLENGE BY GAME END ===");
        System.out.println("ChallengeID: " + challengeID);
        System.out.println("ChallengerScore: " + challengerScore);
        System.out.println("OpponentScore: " + opponentScore);
        
        Challenge challenge = challengeRepository.findById(challengeID)
                .orElseThrow(() -> new RuntimeException("Challenge không tồn tại!"));
        
        // Kiểm tra xem challenge đã được finish chưa
        if (challenge.getChallengerScore() != null && challenge.getOpponentScore() != null) {
            System.out.println("Challenge đã được finish trước đó!");
            return convertToDTO(challenge);
        }
        
        // Kiểm tra trạng thái challenge
        if (!"active".equals(challenge.getStatus()) && !"finished".equals(challenge.getStatus())) {
            System.out.println("Challenge không ở trạng thái active hoặc finished: " + challenge.getStatus());
            return convertToDTO(challenge);
        }
        
        // Cập nhật điểm số và trạng thái ngay lập tức
        challenge.setChallengerScore(challengerScore);
        challenge.setOpponentScore(opponentScore);
        challenge.setStatus("finished");
        challenge.setEndTime(LocalDateTime.now()); // Kết thúc ngay khi game kết thúc
        
        // Save ngay để lock challenge
        challenge = challengeRepository.save(challenge);
        System.out.println("Challenge status updated to finished by game end");
        
        // Xử lý phân phối coin
        return processChallengeResult(challenge, challengerScore, opponentScore);
    }

    // Method riêng để xử lý kết quả challenge
    private ChallengeDTO processChallengeResult(Challenge challenge, Integer challengerScore, Integer opponentScore) {
        // Xác định người thắng
        User winner = null;
        User loser = null;
        Integer totalPot = challenge.getBetAmount() * 2;
        
        System.out.println("Comparing scores: " + challengerScore + " vs " + opponentScore);
        
        if (challengerScore > opponentScore) {
            winner = challenge.getChallenger();
            loser = challenge.getOpponent();
            System.out.println("Winner: " + winner.getUsername());
            System.out.println("Loser: " + loser.getUsername());
        } else if (opponentScore > challengerScore) {
            winner = challenge.getOpponent();
            loser = challenge.getChallenger();
            System.out.println("Winner: " + winner.getUsername());
            System.out.println("Loser: " + loser.getUsername());
        } else {
            System.out.println("TIE GAME - Refunding coins to both players");
            
            // Hòa - hoàn trả coin cho cả 2 người
            UserCoins challengerCoins = userCoinsRepository.findByUser_UserID(challenge.getChallenger().getUserID())
                    .orElseThrow(() -> new RuntimeException("Challenger chưa có coin!"));
            challengerCoins.setCoinAmount(challengerCoins.getCoinAmount() + challenge.getBetAmount());
            userCoinsRepository.save(challengerCoins);
            coinService.createTransaction(challenge.getChallenger().getUserID(), "refund", challenge.getBetAmount(), 
                "Hoàn trả coin do hòa với " + challenge.getOpponent().getUsername() + " - ChallengeID: " + challenge.getChallengeID());
            
            UserCoins opponentCoins = userCoinsRepository.findByUser_UserID(challenge.getOpponent().getUserID())
                    .orElseThrow(() -> new RuntimeException("Opponent chưa có coin!"));
            opponentCoins.setCoinAmount(opponentCoins.getCoinAmount() + challenge.getBetAmount());
            userCoinsRepository.save(opponentCoins);
            coinService.createTransaction(challenge.getOpponent().getUserID(), "refund", challenge.getBetAmount(), 
                "Hoàn trả coin do hòa với " + challenge.getChallenger().getUsername() + " - ChallengeID: " + challenge.getChallengeID());
            
            challenge.setWinner(null);
            
            // Xử lý cược bên ngoài cho trường hợp hòa
            processBets(challenge.getChallengeID(), null);
            
            challenge = challengeRepository.save(challenge);
            System.out.println("=== FINISH CHALLENGE COMPLETED (TIE) ===");
            return convertToDTO(challenge);
        }
        
        // Có người thắng - phân phối coin
        if (winner != null && loser != null) {
            System.out.println("Distributing coins...");
            System.out.println("Total pot: " + totalPot);
            System.out.println("Winner gets: " + totalPot);
            
            // Người thắng nhận toàn bộ tiền cược
            UserCoins winnerCoins = userCoinsRepository.findByUser_UserID(winner.getUserID())
                    .orElseThrow(() -> new RuntimeException("Winner chưa có coin!"));
            System.out.println("Winner current coins: " + winnerCoins.getCoinAmount());
            winnerCoins.setCoinAmount(winnerCoins.getCoinAmount() + totalPot);
            userCoinsRepository.save(winnerCoins);
            System.out.println("Winner new coins: " + winnerCoins.getCoinAmount());
            
            // Tạo transaction
            String winnerDescription = "Thắng thách đấu với " + loser.getUsername() + " (Điểm: " + 
                (winner.equals(challenge.getChallenger()) ? challengerScore : opponentScore) + " vs " + 
                (loser.equals(challenge.getChallenger()) ? challengerScore : opponentScore) + ") - ChallengeID: " + challenge.getChallengeID();
            coinService.createTransaction(winner.getUserID(), "win", totalPot, winnerDescription);
            
            // Người thua mất tiền cược (đã bị trừ từ trước)
            String loserDescription = "Thua thách đấu với " + winner.getUsername() + " (Điểm: " + 
                (loser.equals(challenge.getChallenger()) ? challengerScore : opponentScore) + " vs " + 
                (winner.equals(challenge.getChallenger()) ? challengerScore : opponentScore) + ") - ChallengeID: " + challenge.getChallengeID();
            coinService.createTransaction(loser.getUserID(), "lose", -challenge.getBetAmount(), loserDescription);
            
            challenge.setWinner(winner);
            System.out.println("Set winner to: " + winner.getUsername() + " (ID: " + winner.getUserID() + ")");
        }
        
        // Xử lý cược bên ngoài
        processBets(challenge.getChallengeID(), winner != null ? winner.getUserID() : null);
        
        challenge = challengeRepository.save(challenge);
        System.out.println("Saved challenge with winner: " + (challenge.getWinner() != null ? challenge.getWinner().getUsername() : "null"));
        System.out.println("=== FINISH CHALLENGE COMPLETED ===");
        return convertToDTO(challenge);
    }

    @Override
    public ChallengeDTO getChallengeById(Integer challengeID) {
        Challenge challenge = challengeRepository.findById(challengeID)
                .orElseThrow(() -> new RuntimeException("Challenge không tồn tại!"));
        return convertToDTO(challenge);
    }

    @Override
    public List<ChallengeDTO> getPendingChallenges() {
        return challengeRepository.findPendingChallenges()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDTO> getActiveChallenges() {
        return challengeRepository.findActiveChallenges()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDTO> getFinishedChallenges() {
        return challengeRepository.findFinishedChallenges()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDTO> getUserChallenges(Integer userID) {
        return challengeRepository.findByUserID(userID)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDTO> getAllChallenges() {
        return challengeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDTO> getPendingChallengesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        
        return challengeRepository.findByUserID(user.getUserID())
                .stream()
                .filter(challenge -> "pending".equals(challenge.getStatus()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDTO> getUserChallengesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        
        return challengeRepository.findByUserID(user.getUserID())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDTO> getBettingChallenges() {
        return challengeRepository.findBettingChallenges()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChallengeBetDTO placeBet(Integer challengeID, Integer userID, Integer betOnUserID, Integer betAmount) {
        Challenge challenge = challengeRepository.findById(challengeID)
                .orElseThrow(() -> new RuntimeException("Challenge không tồn tại!"));
        
        if (!"active".equals(challenge.getStatus())) {
            throw new RuntimeException("Challenge không đang diễn ra!");
        }
        
        if (LocalDateTime.now().isAfter(challenge.getEndTime())) {
            throw new RuntimeException("Đã hết thời gian đặt cược!");
        }
        
        // Kiểm tra user có đủ coin không
        UserCoins userCoins = userCoinsRepository.findByUser_UserID(userID)
                .orElseThrow(() -> new RuntimeException("User chưa có coin!"));
        
        if (userCoins.getCoinAmount() < betAmount) {
            throw new RuntimeException("Không đủ coin để đặt cược!");
        }
        
        // Kiểm tra user không phải là người tham gia challenge
        if (userID.equals(challenge.getChallenger().getUserID()) || 
            userID.equals(challenge.getOpponent().getUserID())) {
            throw new RuntimeException("Người tham gia không thể đặt cược!");
        }
        
        // Kiểm tra user đã đặt cược chưa
        List<ChallengeBet> existingBets = challengeBetRepository.findByChallengeIDAndUserID(challengeID, userID);
        if (!existingBets.isEmpty()) {
            throw new RuntimeException("Bạn đã đặt cược cho challenge này!");
        }
        
        // Trừ coin của user
        userCoins.setCoinAmount(userCoins.getCoinAmount() - betAmount);
        userCoinsRepository.save(userCoins);
        
        // Tạo transaction record
        User betOnUser = userRepository.findById(betOnUserID)
                .orElseThrow(() -> new RuntimeException("User đặt cược không tồn tại!"));
        coinService.createTransaction(userID, "bet", -betAmount, 
            "Đặt cược cho " + betOnUser.getUsername());
        
        // Tạo bet
        ChallengeBet bet = new ChallengeBet(challenge, 
            userRepository.findById(userID).orElseThrow(), 
            betOnUser, betAmount);
        bet = challengeBetRepository.save(bet);
        
        return convertToDTO(bet);
    }

    @Override
    @Transactional
    public ChallengeBetDTO placeBetByUsername(Integer challengeID, String username, String betOnUsername, Integer betAmount) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));
        User betOnUser = userRepository.findByUsername(betOnUsername)
                .orElseThrow(() -> new RuntimeException("User đặt cược cho không tồn tại!"));
        return placeBet(challengeID, user.getUserID(), betOnUser.getUserID(), betAmount);
    }

    @Override
    public List<ChallengeBetDTO> getChallengeBets(Integer challengeID) {
        return challengeBetRepository.findByChallenge_ChallengeID(challengeID)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChallengeBetDTO> getUserBets(Integer userID) {
        return challengeBetRepository.findByUser_UserID(userID)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void processBets(Integer challengeID, Integer winnerID) {
        List<ChallengeBet> unprocessedBets = challengeBetRepository.findUnprocessedBets(challengeID);
        
        for (ChallengeBet bet : unprocessedBets) {
            boolean won = winnerID != null && winnerID.equals(bet.getBetOnUser().getUserID());
            bet.setWon(won);
            
            if (won) {
                // Thắng cược - nhận gấp đôi
                bet.setPayoutAmount(bet.getBetAmount() * 2);
                
                UserCoins userCoins = userCoinsRepository.findByUser_UserID(bet.getUser().getUserID())
                        .orElseThrow(() -> new RuntimeException("User chưa có coin!"));
                userCoins.setCoinAmount(userCoins.getCoinAmount() + bet.getPayoutAmount());
                userCoinsRepository.save(userCoins);
                
                // Tạo transaction record
                coinService.createTransaction(bet.getUser().getUserID(), "win", bet.getPayoutAmount(), 
                    "Thắng cược thách đấu");
            } else if (winnerID == null) {
                // Hòa - hoàn trả tiền cược
                bet.setPayoutAmount(bet.getBetAmount());
                
                UserCoins userCoins = userCoinsRepository.findByUser_UserID(bet.getUser().getUserID())
                        .orElseThrow(() -> new RuntimeException("User chưa có coin!"));
                userCoins.setCoinAmount(userCoins.getCoinAmount() + bet.getPayoutAmount());
                userCoinsRepository.save(userCoins);
                
                // Tạo transaction record
                coinService.createTransaction(bet.getUser().getUserID(), "refund", bet.getPayoutAmount(), 
                    "Hoàn trả cược do hòa");
            } else {
                // Thua cược - mất tiền
                bet.setPayoutAmount(0);
                
                // Tạo transaction record
                coinService.createTransaction(bet.getUser().getUserID(), "lose", -bet.getBetAmount(), 
                    "Thua cược thách đấu");
            }
            
            challengeBetRepository.save(bet);
        }
    }

    @Override
    public boolean canUserBet(Integer challengeID, Integer userID) {
        Challenge challenge = challengeRepository.findById(challengeID).orElse(null);
        if (challenge == null || !"active".equals(challenge.getStatus())) {
            return false;
        }
        
        // Kiểm tra user không phải là người tham gia
        if (userID.equals(challenge.getChallenger().getUserID()) || 
            userID.equals(challenge.getOpponent().getUserID())) {
            return false;
        }
        
        // Kiểm tra chưa đặt cược
        List<ChallengeBet> existingBets = challengeBetRepository.findByChallengeIDAndUserID(challengeID, userID);
        return existingBets.isEmpty();
    }

    @Override
    public boolean isChallengeActive(Integer challengeID) {
        Challenge challenge = challengeRepository.findById(challengeID).orElse(null);
        return challenge != null && "active".equals(challenge.getStatus());
    }

    @Override
    public boolean isChallengeFinished(Integer challengeID) {
        Challenge challenge = challengeRepository.findById(challengeID)
                .orElseThrow(() -> new RuntimeException("Challenge không tồn tại!"));
        return "finished".equals(challenge.getStatus());
    }

    // Method để tự động cập nhật trạng thái thách đấu hết hạn
    @Override
    @Transactional
    public void updateExpiredChallenges() {
        LocalDateTime now = LocalDateTime.now();
        
        // Cập nhật thách đấu active đã hết hạn
        List<Challenge> activeChallenges = challengeRepository.findByStatus("active");
        for (Challenge challenge : activeChallenges) {
            if (challenge.getEndTime() != null && now.isAfter(challenge.getEndTime())) {
                // Chỉ xử lý nếu chưa có điểm số (chưa được finish)
                if (challenge.getChallengerScore() == null && challenge.getOpponentScore() == null) {
                    challenge.setChallengerScore(0);
                    challenge.setOpponentScore(0);
                    challenge.setStatus("finished");
                    challenge.setEndTime(now);
                    
                    // Hoàn trả coin cho cả 2 người chơi (vì không ai chơi)
                    UserCoins challengerCoins = userCoinsRepository.findByUser_UserID(challenge.getChallenger().getUserID())
                            .orElse(null);
                    if (challengerCoins != null) {
                        challengerCoins.setCoinAmount(challengerCoins.getCoinAmount() + challenge.getBetAmount());
                        userCoinsRepository.save(challengerCoins);
                        coinService.createTransaction(challenge.getChallenger().getUserID(), "refund", challenge.getBetAmount(), 
                            "Hoàn trả coin do thách đấu hết hạn (không ai chơi)");
                    }
                    
                    UserCoins opponentCoins = userCoinsRepository.findByUser_UserID(challenge.getOpponent().getUserID())
                            .orElse(null);
                    if (opponentCoins != null) {
                        opponentCoins.setCoinAmount(opponentCoins.getCoinAmount() + challenge.getBetAmount());
                        userCoinsRepository.save(opponentCoins);
                        coinService.createTransaction(challenge.getOpponent().getUserID(), "refund", challenge.getBetAmount(), 
                            "Hoàn trả coin do thách đấu hết hạn (không ai chơi)");
                    }
                    
                    challengeRepository.save(challenge);
                }
                // Nếu đã có điểm số, không làm gì cả (đã được finishChallenge xử lý)
            }
        }
        
        // Cập nhật thách đấu pending đã quá cũ (24 giờ)
        List<Challenge> pendingChallenges = challengeRepository.findByStatus("pending");
        for (Challenge challenge : pendingChallenges) {
            if (challenge.getCreatedAt() != null && now.isAfter(challenge.getCreatedAt().plusHours(24))) {
                // Hoàn trả coin cho challenger
                UserCoins challengerCoins = userCoinsRepository.findByUser_UserID(challenge.getChallenger().getUserID())
                        .orElse(null);
                if (challengerCoins != null) {
                    challengerCoins.setCoinAmount(challengerCoins.getCoinAmount() + challenge.getBetAmount());
                    userCoinsRepository.save(challengerCoins);
                    coinService.createTransaction(challenge.getChallenger().getUserID(), "refund", challenge.getBetAmount(), 
                        "Hoàn trả coin do thách đấu hết hạn");
                }
                
                challenge.setStatus("expired");
                challengeRepository.save(challenge);
            }
        }
    }

    // Helper methods
    private ChallengeDTO convertToDTO(Challenge challenge) {
        ChallengeDTO dto = new ChallengeDTO(
            challenge.getChallengeID(),
            challenge.getChallenger().getUserID(),
            challenge.getChallenger().getUsername(),
            challenge.getOpponent().getUserID(),
            challenge.getOpponent().getUsername(),
            challenge.getGame().getGameID(),
            challenge.getGame().getTitle(),
            challenge.getBetAmount(),
            challenge.getStatus(),
            challenge.getStartTime(),
            challenge.getEndTime(),
            challenge.getChallengerScore(),
            challenge.getOpponentScore(),
            challenge.getWinner() != null ? challenge.getWinner().getUserID() : null,
            challenge.getWinner() != null ? challenge.getWinner().getUsername() : null,
            challenge.getCreatedAt()
        );
        
        // Thêm thông tin betting nếu challenge đang active
        if ("active".equals(challenge.getStatus()) && challenge.getEndTime() != null) {
            Integer totalBets = challengeBetRepository.getTotalBetsInChallenge(challenge.getChallengeID());
            Integer challengerBets = challengeBetRepository.getTotalBetsForUserInChallenge(
                challenge.getChallengeID(), challenge.getChallenger().getUserID());
            Integer opponentBets = challengeBetRepository.getTotalBetsForUserInChallenge(
                challenge.getChallengeID(), challenge.getOpponent().getUserID());
            
            dto.setTotalBets(totalBets != null ? totalBets : 0);
            dto.setChallengerBets(challengerBets != null ? challengerBets : 0);
            dto.setOpponentBets(opponentBets != null ? opponentBets : 0);
            
            // Tính thời gian còn lại
            long timeRemaining = ChronoUnit.MILLIS.between(LocalDateTime.now(), challenge.getEndTime());
            dto.setTimeRemaining(Math.max(0, timeRemaining));
        }
        
        return dto;
    }

    private ChallengeBetDTO convertToDTO(ChallengeBet bet) {
        return new ChallengeBetDTO(
            bet.getBetID(),
            bet.getChallenge().getChallengeID(),
            bet.getUser().getUserID(),
            bet.getUser().getUsername(),
            bet.getBetOnUser().getUserID(),
            bet.getBetOnUser().getUsername(),
            bet.getBetAmount(),
            bet.getWon(),
            bet.getPayoutAmount(),
            bet.getCreatedAt()
        );
    }
} 