package com.WebSiteChoiGame.DoAnJ2EE.controller;

import com.WebSiteChoiGame.DoAnJ2EE.dto.StreamDTO;
import com.WebSiteChoiGame.DoAnJ2EE.dto.StreamChatDTO;
import com.WebSiteChoiGame.DoAnJ2EE.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/streams")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class StreamController {

    @Autowired
    private StreamService streamService;

    // Tạo stream mới
    @PostMapping("/create")
    public ResponseEntity<StreamDTO> createStream(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Received request: " + request);
            
            Object streamerIdOrUsername = request.get("streamerId");
            Object gameIdObj = request.get("gameId");
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            
            // Convert gameId to Integer
            Integer gameId;
            if (gameIdObj instanceof Integer) {
                gameId = (Integer) gameIdObj;
            } else if (gameIdObj instanceof String) {
                gameId = Integer.parseInt((String) gameIdObj);
            } else {
                throw new RuntimeException("Invalid gameId type: " + gameIdObj.getClass().getSimpleName());
            }

            System.out.println("Creating stream with: streamerId/username=" + streamerIdOrUsername + 
                             ", type=" + (streamerIdOrUsername != null ? streamerIdOrUsername.getClass().getSimpleName() : "null") +
                             ", gameId=" + gameId + ", title=" + title);
            
            StreamDTO stream;
            if (streamerIdOrUsername instanceof Integer) {
                // Nếu là ID
                System.out.println("Using ID method");
                stream = streamService.createStream((Integer) streamerIdOrUsername, gameId, title, description);
            } else {
                // Nếu là username
                System.out.println("Using username method: " + streamerIdOrUsername);
                stream = streamService.createStreamByUsername((String) streamerIdOrUsername, gameId, title, description);
            }
            System.out.println("Stream created successfully: " + stream);
            return ResponseEntity.ok(stream);
        } catch (Exception e) {
            System.err.println("Error creating stream: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Bắt đầu stream
    @PostMapping("/{streamId}/start")
    public ResponseEntity<StreamDTO> startStream(@PathVariable Integer streamId) {
        try {
            StreamDTO stream = streamService.startStream(streamId);
            return ResponseEntity.ok(stream);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Dừng stream
    @PostMapping("/{streamId}/stop")
    public ResponseEntity<StreamDTO> stopStream(@PathVariable Integer streamId) {
        try {
            StreamDTO stream = streamService.stopStream(streamId);
            return ResponseEntity.ok(stream);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy thông tin stream
    @GetMapping("/{streamId}")
    public ResponseEntity<StreamDTO> getStream(@PathVariable Integer streamId) {
        try {
            StreamDTO stream = streamService.getStreamById(streamId);
            return ResponseEntity.ok(stream);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy stream theo key
    @GetMapping("/key/{streamKey}")
    public ResponseEntity<StreamDTO> getStreamByKey(@PathVariable String streamKey) {
        try {
            StreamDTO stream = streamService.getStreamByKey(streamKey);
            return ResponseEntity.ok(stream);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy danh sách stream đang live
    @GetMapping("/live")
    public ResponseEntity<List<StreamDTO>> getLiveStreams() {
        try {
            List<StreamDTO> streams = streamService.getLiveStreams();
            return ResponseEntity.ok(streams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy stream đang live theo game
    @GetMapping("/live/game/{gameId}")
    public ResponseEntity<List<StreamDTO>> getLiveStreamsByGame(@PathVariable Integer gameId) {
        try {
            List<StreamDTO> streams = streamService.getLiveStreamsByGame(gameId);
            return ResponseEntity.ok(streams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy stream đang live của streamer theo ID
    @GetMapping("/live/streamer/{streamerId}")
    public ResponseEntity<StreamDTO> getLiveStreamByStreamer(@PathVariable Integer streamerId) {
        try {
            StreamDTO stream = streamService.getLiveStreamByStreamer(streamerId);
            return stream != null ? ResponseEntity.ok(stream) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy stream đang live của streamer theo username
    @GetMapping("/live/streamer/username/{username}")
    public ResponseEntity<StreamDTO> getLiveStreamByStreamerUsername(@PathVariable String username) {
        try {
            StreamDTO stream = streamService.getLiveStreamByStreamerUsername(username);
            return stream != null ? ResponseEntity.ok(stream) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Thêm viewer vào stream
    @PostMapping("/{streamId}/viewers")
    public ResponseEntity<Void> addViewer(@PathVariable Integer streamId, @RequestBody Map<String, Object> request) {
        try {
            System.out.println("=== addViewer called ===");
            System.out.println("streamId: " + streamId);
            System.out.println("request: " + request);
            
            Object userIdOrUsername = request.get("userId");
            System.out.println("userIdOrUsername: " + userIdOrUsername + " (type: " + (userIdOrUsername != null ? userIdOrUsername.getClass().getSimpleName() : "null") + ")");
            
            if (userIdOrUsername instanceof Integer) {
                System.out.println("Using Integer method");
                streamService.addViewer(streamId, (Integer) userIdOrUsername);
            } else if (userIdOrUsername instanceof String) {
                System.out.println("Using String method");
                streamService.addViewerByUsername(streamId, (String) userIdOrUsername);
            } else {
                System.out.println("Invalid type: " + (userIdOrUsername != null ? userIdOrUsername.getClass().getSimpleName() : "null"));
                return ResponseEntity.badRequest().build();
            }
            System.out.println("=== addViewer completed successfully ===");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("=== Error adding viewer ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Xóa viewer khỏi stream theo ID
    @DeleteMapping("/{streamId}/viewers/id/{userId}")
    public ResponseEntity<Void> removeViewer(@PathVariable Integer streamId, @PathVariable Integer userId) {
        try {
            streamService.removeViewer(streamId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Xóa viewer khỏi stream theo username
    @DeleteMapping("/{streamId}/viewers/username/{username}")
    public ResponseEntity<Void> removeViewerByUsername(@PathVariable Integer streamId, @PathVariable String username) {
        try {
            streamService.removeViewerByUsername(streamId, username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error removing viewer: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy số lượng viewer
    @GetMapping("/{streamId}/viewers/count")
    public ResponseEntity<Map<String, Integer>> getViewerCount(@PathVariable Integer streamId) {
        try {
            Integer count = streamService.getViewerCount(streamId);
            return ResponseEntity.ok(Map.of("viewerCount", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Gửi tin nhắn chat
    @PostMapping("/{streamId}/chat")
    public ResponseEntity<StreamChatDTO> sendChatMessage(@PathVariable Integer streamId, @RequestBody Map<String, Object> request) {
        try {
            Object userIdOrUsername = request.get("userId");
            String message = (String) request.get("message");
            
            StreamChatDTO chat;
            if (userIdOrUsername instanceof Integer) {
                chat = streamService.sendChatMessage(streamId, (Integer) userIdOrUsername, message);
            } else if (userIdOrUsername instanceof String) {
                chat = streamService.sendChatMessageByUsername(streamId, (String) userIdOrUsername, message);
            } else {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(chat);
        } catch (Exception e) {
            System.err.println("Error sending chat message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy tin nhắn chat
    @GetMapping("/{streamId}/chat")
    public ResponseEntity<List<StreamChatDTO>> getChatMessages(@PathVariable Integer streamId, @RequestParam(defaultValue = "50") int limit) {
        try {
            List<StreamChatDTO> messages = streamService.getChatMessages(streamId, limit);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Xóa stream
    @DeleteMapping("/{streamId}")
    public ResponseEntity<Void> deleteStream(@PathVariable Integer streamId) {
        try {
            streamService.deleteStream(streamId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 