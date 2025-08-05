package com.WebSiteChoiGame.DoAnJ2EE.repository;

import com.WebSiteChoiGame.DoAnJ2EE.entity.StreamViewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreamViewerRepository extends JpaRepository<StreamViewer, Integer> {
    
    // Tìm viewer theo stream
    List<StreamViewer> findByStreamStreamId(Integer streamId);
    
    // Tìm viewer theo user
    List<StreamViewer> findByUserUserID(Integer userId);
    
    // Tìm viewer đang xem (chưa có leftAt)
    @Query("SELECT sv FROM StreamViewer sv WHERE sv.stream.streamId = :streamId AND sv.leftAt IS NULL")
    List<StreamViewer> findActiveViewersByStream(@Param("streamId") Integer streamId);
    
    // Tìm viewer đang xem stream cụ thể
    @Query("SELECT sv FROM StreamViewer sv WHERE sv.stream.streamId = :streamId AND sv.user.userID = :userId AND sv.leftAt IS NULL")
    Optional<StreamViewer> findActiveViewer(@Param("streamId") Integer streamId, @Param("userId") Integer userId);
    
    // Đếm số viewer đang xem
    @Query("SELECT COUNT(sv) FROM StreamViewer sv WHERE sv.stream.streamId = :streamId AND sv.leftAt IS NULL")
    Long countActiveViewersByStream(@Param("streamId") Integer streamId);
} 