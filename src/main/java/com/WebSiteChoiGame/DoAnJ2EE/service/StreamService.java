package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.dto.StreamDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.StreamChatDTO;
import java.util.List;

public interface StreamService {
    
    // Tạo stream mới
    StreamDTO createStream(Integer streamerId, Integer gameId, String title, String description);
    
    // Tạo stream mới theo username
    StreamDTO createStreamByUsername(String username, Integer gameId, String title, String description);
    
    // Bắt đầu stream
    StreamDTO startStream(Integer streamId);
    
    // Dừng stream
    StreamDTO stopStream(Integer streamId);
    
    // Lấy thông tin stream
    StreamDTO getStreamById(Integer streamId);
    
    // Lấy stream theo stream key
    StreamDTO getStreamByKey(String streamKey);
    
    // Lấy danh sách stream đang live
    List<StreamDTO> getLiveStreams();
    
    // Lấy stream đang live theo game
    List<StreamDTO> getLiveStreamsByGame(Integer gameId);
    
    // Lấy stream đang live của streamer
    StreamDTO getLiveStreamByStreamer(Integer streamerId);
    
    // Lấy stream đang live của streamer theo username
    StreamDTO getLiveStreamByStreamerUsername(String username);
    
    // Thêm viewer vào stream
    void addViewer(Integer streamId, Integer userId);
    
    // Thêm viewer vào stream theo username
    void addViewerByUsername(Integer streamId, String username);
    
    // Xóa viewer khỏi stream
    void removeViewer(Integer streamId, Integer userId);
    
    // Xóa viewer khỏi stream theo username
    void removeViewerByUsername(Integer streamId, String username);
    
    // Lấy số lượng viewer
    Integer getViewerCount(Integer streamId);
    
    // Gửi tin nhắn chat
    StreamChatDTO sendChatMessage(Integer streamId, Integer userId, String message);
    
    // Gửi tin nhắn chat theo username
    StreamChatDTO sendChatMessageByUsername(Integer streamId, String username, String message);
    
    // Lấy tin nhắn chat của stream
    List<StreamChatDTO> getChatMessages(Integer streamId, int limit);
    
    // Xóa stream
    void deleteStream(Integer streamId);
} 