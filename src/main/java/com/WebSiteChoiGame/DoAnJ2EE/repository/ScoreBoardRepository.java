package com.WebSiteChoiGame.DoAnJ2EE.repository;

import com.WebSiteChoiGame.DoAnJ2EE.entity.ScoreBoard;
import com.WebSiteChoiGame.DoAnJ2EE.entity.User;
import com.WebSiteChoiGame.DoAnJ2EE.entity.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
 
@Repository
public interface ScoreBoardRepository extends JpaRepository<ScoreBoard, Integer> {
    @Query("SELECT s FROM ScoreBoard s ORDER BY s.highScore DESC")
    List<ScoreBoard> findTopByOrderByScoreDesc(Pageable pageable);
    
    Optional<ScoreBoard> findByUserAndGame(User user, Game game);
    
    @Query("SELECT s FROM ScoreBoard s WHERE s.game = :game ORDER BY s.highScore DESC")
    List<ScoreBoard> findByGameOrderByHighScoreDesc(Game game);
    
    @Query("SELECT s FROM ScoreBoard s WHERE s.user = :user AND s.game = :game")
    List<ScoreBoard> findAllByUserAndGame(User user, Game game);
    
    @Query("SELECT s FROM ScoreBoard s WHERE s.game.gameID = :gameId ORDER BY s.highScore DESC")
    List<ScoreBoard> findByGame_GameIDOrderByHighScoreDesc(@Param("gameId") Integer gameId, Pageable pageable);
} 