package com.WebSiteChoiGame.DoAnJ2EE.repository;

import com.WebSiteChoiGame.DoAnJ2EE.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Integer> {
    
    // Lấy điểm số cao nhất của user trong game trong khoảng thời gian
    @Query("SELECT MAX(gs.score) FROM GameSession gs WHERE gs.user.userID = :userID AND gs.game.gameID = :gameID AND gs.playedAt BETWEEN :startTime AND :endTime")
    Integer findScoreByUserAndGameAndTimeRange(@Param("userID") Integer userID, 
                                             @Param("gameID") Integer gameID,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);
    
    // Lấy tất cả game sessions của user trong game
    List<GameSession> findByUser_UserIDAndGame_GameID(Integer userID, Integer gameID);
    
    // Lấy game sessions theo thời gian
    List<GameSession> findByPlayedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
} 