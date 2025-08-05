package com.WebSiteChoiGame.DoAnJ2EE.repository;

import com.WebSiteChoiGame.DoAnJ2EE.entity.StreamChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreamChatRepository extends JpaRepository<StreamChat, Integer> {
    
    // Tìm tin nhắn theo stream
    List<StreamChat> findByStreamStreamIdOrderBySentAtDesc(Integer streamId);
    
    // Tìm tin nhắn theo stream với giới hạn
    @Query("SELECT sc FROM StreamChat sc WHERE sc.stream.streamId = :streamId ORDER BY sc.sentAt DESC")
    List<StreamChat> findRecentChatsByStream(@Param("streamId") Integer streamId);
    
    // Tìm tin nhắn theo user
    List<StreamChat> findByUserUserIDOrderBySentAtDesc(Integer userId);
    
    // Đếm số tin nhắn trong stream
    @Query("SELECT COUNT(sc) FROM StreamChat sc WHERE sc.stream.streamId = :streamId")
    Long countChatsByStream(@Param("streamId") Integer streamId);
} 