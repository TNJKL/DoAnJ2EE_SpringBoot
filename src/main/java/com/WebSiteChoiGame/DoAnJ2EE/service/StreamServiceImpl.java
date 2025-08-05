package com.WebSiteChoiGame.DoAnJ2EE.service;

import com.WebSiteChoiGame.DoAnJ2EE.dto.StreamDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.StreamChatDTO;
import com.WebSiteChoiGame.DoAnJ2EE.entity.*;
import com.WebSiteChoiGame.DoAnJ2EE.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class StreamServiceImpl implements StreamService {

    @Autowired
    private StreamRepository streamRepository;

    @Autowired
    private StreamViewerRepository streamViewerRepository;

    @Autowired
    private StreamChatRepository streamChatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public StreamDTO createStream(Integer streamerId, Integer gameId, String title, String description) {
        User streamer = userRepository.findById(streamerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        // Tạo stream key duy nhất
        String streamKey = UUID.randomUUID().toString();

        Stream stream = new Stream(streamer, game, title, description, streamKey);
        stream = streamRepository.save(stream);

        return convertToDTO(stream);
    }

    @Override
    public StreamDTO createStreamByUsername(String username, Integer gameId, String title, String description) {
        System.out.println("createStreamByUsername called with: username=" + username + ", gameId=" + gameId + ", title=" + title);
        
        User streamer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        System.out.println("Found user: " + streamer.getUsername() + " with ID: " + streamer.getUserID());
        
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        System.out.println("Found game: " + game.getTitle() + " with ID: " + game.getGameID());

        // Tạo stream key duy nhất
        String streamKey = UUID.randomUUID().toString();
        System.out.println("Generated stream key: " + streamKey);

        Stream stream = new Stream(streamer, game, title, description, streamKey);
        stream = streamRepository.save(stream);
        System.out.println("Stream saved with ID: " + stream.getStreamId());

        return convertToDTO(stream);
    }

    @Override
    public StreamDTO startStream(Integer streamId) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new RuntimeException("Stream not found"));

        stream.setIsLive(true);
        stream.setStartedAt(LocalDateTime.now());
        stream.setViewerCount(0);
        stream = streamRepository.save(stream);

        return convertToDTO(stream);
    }

    @Override
    public StreamDTO stopStream(Integer streamId) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new RuntimeException("Stream not found"));

        stream.setIsLive(false);
        stream.setEndedAt(LocalDateTime.now());
        stream = streamRepository.save(stream);

        // Xóa tất cả viewer đang xem
        List<StreamViewer> activeViewers = streamViewerRepository.findActiveViewersByStream(streamId);
        for (StreamViewer viewer : activeViewers) {
            viewer.setLeftAt(LocalDateTime.now());
            streamViewerRepository.save(viewer);
        }

        return convertToDTO(stream);
    }

    @Override
    public StreamDTO getStreamById(Integer streamId) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new RuntimeException("Stream not found"));
        return convertToDTO(stream);
    }

    @Override
    public StreamDTO getStreamByKey(String streamKey) {
        Stream stream = streamRepository.findByStreamKey(streamKey)
                .orElseThrow(() -> new RuntimeException("Stream not found"));
        return convertToDTO(stream);
    }

    @Override
    public List<StreamDTO> getLiveStreams() {
        List<Stream> liveStreams = streamRepository.findByIsLiveTrue();
        return liveStreams.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StreamDTO> getLiveStreamsByGame(Integer gameId) {
        List<Stream> liveStreams = streamRepository.findLiveStreamsByGame(gameId);
        return liveStreams.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StreamDTO getLiveStreamByStreamer(Integer streamerId) {
        Stream stream = streamRepository.findLiveStreamByStreamer(streamerId)
                .orElse(null);
        return stream != null ? convertToDTO(stream) : null;
    }

    @Override
    public StreamDTO getLiveStreamByStreamerUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);
        if (user == null) return null;
        
        Stream stream = streamRepository.findLiveStreamByStreamer(user.getUserID())
                .orElse(null);
        return stream != null ? convertToDTO(stream) : null;
    }

    @Override
    public void addViewer(Integer streamId, Integer userId) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new RuntimeException("Stream not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kiểm tra xem user đã đang xem chưa
        Optional<StreamViewer> existingViewer = streamViewerRepository.findActiveViewer(streamId, userId);
        if (existingViewer.isPresent()) {
            return; // Đã đang xem rồi
        }

        StreamViewer viewer = new StreamViewer(stream, user);
        streamViewerRepository.save(viewer);

        // Cập nhật số lượng viewer
        Long viewerCount = streamViewerRepository.countActiveViewersByStream(streamId);
        stream.setViewerCount(viewerCount.intValue());
        streamRepository.save(stream);
    }

    @Override
    public void addViewerByUsername(Integer streamId, String username) {
        System.out.println("addViewerByUsername called with: streamId=" + streamId + ", username=" + username);
        
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new RuntimeException("Stream not found"));
        System.out.println("Found stream: " + stream.getTitle());
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        System.out.println("Found user: " + user.getUsername() + " with ID: " + user.getUserID());

        // Kiểm tra xem user đã đang xem chưa
        Optional<StreamViewer> existingViewer = streamViewerRepository.findActiveViewer(streamId, user.getUserID());
        if (existingViewer.isPresent()) {
            System.out.println("User already viewing this stream");
            return; // Đã đang xem rồi
        }

        StreamViewer viewer = new StreamViewer(stream, user);
        streamViewerRepository.save(viewer);
        System.out.println("Added viewer successfully");

        // Cập nhật số lượng viewer
        Long viewerCount = streamViewerRepository.countActiveViewersByStream(streamId);
        stream.setViewerCount(viewerCount.intValue());
        streamRepository.save(stream);
        System.out.println("Updated viewer count to: " + viewerCount);
    }

    @Override
    public void removeViewer(Integer streamId, Integer userId) {
        Optional<StreamViewer> viewer = streamViewerRepository.findActiveViewer(streamId, userId);
        if (viewer.isPresent()) {
            StreamViewer activeViewer = viewer.get();
            activeViewer.setLeftAt(LocalDateTime.now());
            streamViewerRepository.save(activeViewer);

            // Cập nhật số lượng viewer
            Stream stream = streamRepository.findById(streamId).orElse(null);
            if (stream != null) {
                Long viewerCount = streamViewerRepository.countActiveViewersByStream(streamId);
                stream.setViewerCount(viewerCount.intValue());
                streamRepository.save(stream);
            }
        }
    }

    @Override
    public void removeViewerByUsername(Integer streamId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        Optional<StreamViewer> viewer = streamViewerRepository.findActiveViewer(streamId, user.getUserID());
        if (viewer.isPresent()) {
            StreamViewer activeViewer = viewer.get();
            activeViewer.setLeftAt(LocalDateTime.now());
            streamViewerRepository.save(activeViewer);

            // Cập nhật số lượng viewer
            Stream stream = streamRepository.findById(streamId).orElse(null);
            if (stream != null) {
                Long viewerCount = streamViewerRepository.countActiveViewersByStream(streamId);
                stream.setViewerCount(viewerCount.intValue());
                streamRepository.save(stream);
            }
        }
    }

    @Override
    public Integer getViewerCount(Integer streamId) {
        Long count = streamViewerRepository.countActiveViewersByStream(streamId);
        return count.intValue();
    }

    @Override
    public StreamChatDTO sendChatMessage(Integer streamId, Integer userId, String message) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new RuntimeException("Stream not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StreamChat chat = new StreamChat(stream, user, message);
        chat = streamChatRepository.save(chat);

        return convertToChatDTO(chat);
    }

    @Override
    public StreamChatDTO sendChatMessageByUsername(Integer streamId, String username, String message) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new RuntimeException("Stream not found"));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        StreamChat chat = new StreamChat(stream, user, message);
        chat = streamChatRepository.save(chat);

        return convertToChatDTO(chat);
    }

    @Override
    public List<StreamChatDTO> getChatMessages(Integer streamId, int limit) {
        List<StreamChat> chats = streamChatRepository.findByStreamStreamIdOrderBySentAtDesc(streamId);
        if (limit > 0 && chats.size() > limit) {
            chats = chats.subList(0, limit);
        }
        
        return chats.stream()
                .map(this::convertToChatDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStream(Integer streamId) {
        Stream stream = streamRepository.findById(streamId)
                .orElseThrow(() -> new RuntimeException("Stream not found"));
        
        // Xóa tất cả viewer
        List<StreamViewer> viewers = streamViewerRepository.findByStreamStreamId(streamId);
        streamViewerRepository.deleteAll(viewers);
        
        // Xóa tất cả chat
        List<StreamChat> chats = streamChatRepository.findByStreamStreamIdOrderBySentAtDesc(streamId);
        streamChatRepository.deleteAll(chats);
        
        // Xóa stream
        streamRepository.delete(stream);
    }

    // Helper methods
    private StreamDTO convertToDTO(Stream stream) {
        StreamDTO dto = new StreamDTO();
        dto.setStreamId(stream.getStreamId());
        dto.setStreamerId(stream.getStreamer().getUserID());
        dto.setStreamerUsername(stream.getStreamer().getUsername());
        dto.setGameId(stream.getGame().getGameID());
        dto.setGameTitle(stream.getGame().getTitle());
        dto.setTitle(stream.getTitle());
        dto.setDescription(stream.getDescription());
        dto.setStreamKey(stream.getStreamKey());
        dto.setIsLive(stream.getIsLive());
        dto.setViewerCount(stream.getViewerCount());
        dto.setStartedAt(stream.getStartedAt());
        dto.setEndedAt(stream.getEndedAt());
        dto.setCreatedAt(stream.getCreatedAt());
        return dto;
    }

    private StreamChatDTO convertToChatDTO(StreamChat chat) {
        StreamChatDTO dto = new StreamChatDTO();
        dto.setChatId(chat.getChatId());
        dto.setStreamId(chat.getStream().getStreamId());
        dto.setUserId(chat.getUser().getUserID());
        dto.setUsername(chat.getUser().getUsername());
        dto.setMessage(chat.getMessage());
        dto.setSentAt(chat.getSentAt());
        return dto;
    }
} 