package com.WebSiteChoiGame.DoAnJ2EE.repository;

import com.WebSiteChoiGame.DoAnJ2EE.entity.ChallengeBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeBetRepository extends JpaRepository<ChallengeBet, Integer> {
    
    // Lấy tất cả cược của một challenge
    List<ChallengeBet> findByChallenge_ChallengeID(Integer challengeID);
    
    // Lấy cược của một user
    List<ChallengeBet> findByUser_UserID(Integer userID);
    
    // Lấy cược của một user trong một challenge cụ thể
    @Query("SELECT cb FROM ChallengeBet cb WHERE cb.challenge.challengeID = :challengeID AND cb.user.userID = :userID")
    List<ChallengeBet> findByChallengeIDAndUserID(@Param("challengeID") Integer challengeID, @Param("userID") Integer userID);
    
    // Lấy tổng số cược cho một user trong challenge
    @Query("SELECT SUM(cb.betAmount) FROM ChallengeBet cb WHERE cb.challenge.challengeID = :challengeID AND cb.betOnUser.userID = :userID")
    Integer getTotalBetsForUserInChallenge(@Param("challengeID") Integer challengeID, @Param("userID") Integer userID);
    
    // Lấy tổng số cược trong một challenge
    @Query("SELECT SUM(cb.betAmount) FROM ChallengeBet cb WHERE cb.challenge.challengeID = :challengeID")
    Integer getTotalBetsInChallenge(@Param("challengeID") Integer challengeID);
    
    // Lấy cược chưa xử lý (won = null)
    @Query("SELECT cb FROM ChallengeBet cb WHERE cb.challenge.challengeID = :challengeID AND cb.won IS NULL")
    List<ChallengeBet> findUnprocessedBets(@Param("challengeID") Integer challengeID);
} 