package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.dto.ChallengeDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.ChallengeBetDTO;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Challenge;
import com.WebSiteChoiGame.DoAnJ2EE.entity.ChallengeBet;

import java.util.List;

public interface ChallengeService {
    // Challenge methods
    ChallengeDTO createChallenge(Integer challengerID, Integer opponentID, Integer gameID, Integer betAmount);
    ChallengeDTO createChallengeByUsername(String challengerUsername, String opponentUsername, Integer gameID, Integer betAmount);
    ChallengeDTO acceptChallenge(Integer challengeID, Integer opponentID);
    ChallengeDTO acceptChallengeByUsername(Integer challengeID, String opponentUsername);
    ChallengeDTO declineChallenge(Integer challengeID, Integer opponentID);
    ChallengeDTO declineChallengeByUsername(Integer challengeID, String opponentUsername);
    ChallengeDTO startChallenge(Integer challengeID);
    ChallengeDTO finishChallenge(Integer challengeID, Integer challengerScore, Integer opponentScore);
    ChallengeDTO finishChallengeByGameEnd(Integer challengeID, Integer challengerScore, Integer opponentScore);
    ChallengeDTO getChallengeById(Integer challengeID);
    List<ChallengeDTO> getPendingChallenges();
    List<ChallengeDTO> getActiveChallenges();
    List<ChallengeDTO> getFinishedChallenges();
    List<ChallengeDTO> getUserChallenges(Integer userID);
    List<ChallengeDTO> getAllChallenges();
    List<ChallengeDTO> getBettingChallenges();
    List<ChallengeDTO> getPendingChallengesByUsername(String username);
    List<ChallengeDTO> getUserChallengesByUsername(String username);
    
    // ChallengeBet methods
    ChallengeBetDTO placeBet(Integer challengeID, Integer userID, Integer betOnUserID, Integer betAmount);
    ChallengeBetDTO placeBetByUsername(Integer challengeID, String username, String betOnUsername, Integer betAmount);
    List<ChallengeBetDTO> getChallengeBets(Integer challengeID);
    List<ChallengeBetDTO> getUserBets(Integer userID);
    void processBets(Integer challengeID, Integer winnerID);
    
    // Utility methods
    boolean canUserBet(Integer challengeID, Integer userID);
    boolean isChallengeActive(Integer challengeID);
    boolean isChallengeFinished(Integer challengeID);
    void updateExpiredChallenges();
} 