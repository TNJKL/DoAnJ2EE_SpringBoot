package com.WebSiteChoiGame.DoAnJ2EE.repository;

import com.WebSiteChoiGame.DoAnJ2EE.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    
    // Lấy thách đấu theo trạng thái
    List<Challenge> findByStatus(String status);
    
    // Lấy thách đấu của user (là challenger hoặc opponent)
    @Query("SELECT c FROM Challenge c WHERE c.challenger.userID = :userID OR c.opponent.userID = :userID ORDER BY c.createdAt DESC")
    List<Challenge> findByUserID(@Param("userID") Integer userID);
    
    // Lấy thách đấu đang chờ (pending)
    @Query("SELECT c FROM Challenge c WHERE c.status = 'pending' ORDER BY c.createdAt DESC")
    List<Challenge> findPendingChallenges();
    
    // Lấy thách đấu đang diễn ra (active)
    @Query("SELECT c FROM Challenge c WHERE c.status = 'active' ORDER BY c.startTime DESC")
    List<Challenge> findActiveChallenges();
    
    // Lấy thách đấu đã hoàn thành (finished)
    @Query("SELECT c FROM Challenge c WHERE c.status = 'finished' ORDER BY c.endTime DESC")
    List<Challenge> findFinishedChallenges();
    
    // Lấy thách đấu theo game
    List<Challenge> findByGame_GameID(Integer gameID);
    
    // Lấy thách đấu có thể đặt cược (active và chưa kết thúc)
    @Query("SELECT c FROM Challenge c WHERE c.status = 'active' AND c.endTime > CURRENT_TIMESTAMP ORDER BY c.startTime ASC")
    List<Challenge> findBettingChallenges();
} 