package com.WebSiteChoiGame.DoAnJ2EE.repository;

import com.WebSiteChoiGame.DoAnJ2EE.entity.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreamRepository extends JpaRepository<Stream, Integer> {
    
    // Tìm stream theo streamer
    List<Stream> findByStreamerUserID(Integer streamerId);
    
    // Tìm stream theo game
    List<Stream> findByGameGameID(Integer gameId);
    
    // Tìm stream đang live
    List<Stream> findByIsLiveTrue();
    
    // Tìm stream theo stream key
    Optional<Stream> findByStreamKey(String streamKey);
    
    // Tìm stream đang live theo game
    @Query("SELECT s FROM Stream s WHERE s.game.gameID = :gameId AND s.isLive = true")
    List<Stream> findLiveStreamsByGame(@Param("gameId") Integer gameId);
    
    // Tìm stream đang live theo streamer
    @Query("SELECT s FROM Stream s WHERE s.streamer.userID = :streamerId AND s.isLive = true")
    Optional<Stream> findLiveStreamByStreamer(@Param("streamerId") Integer streamerId);
    
    // Đếm số stream đang live
    @Query("SELECT COUNT(s) FROM Stream s WHERE s.isLive = true")
    Long countLiveStreams();
} 