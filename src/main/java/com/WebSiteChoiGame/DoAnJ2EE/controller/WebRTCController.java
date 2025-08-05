package com.WebSiteChoiGame.DoAnJ2EE.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.DestinationVariable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebRTCController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private static final Map<String, String> streamerSessions = new ConcurrentHashMap<>();
    private static final Map<String, String> viewerSessions = new ConcurrentHashMap<>();
    
    public WebRTCController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        System.out.println("=== WebRTCController initialized ===");
    }
    
    @MessageMapping("/stream/{streamId}/join")
    public void handleViewerJoin(@DestinationVariable String streamId, @Payload Map<String, Object> message) {
        System.out.println("Viewer join request for stream: " + streamId);
        String viewerId = (String) message.get("viewerId");
        viewerSessions.put(streamId, viewerId);
        
        // Notify streamer about new viewer
        messagingTemplate.convertAndSend("/topic/stream/" + streamId + "/viewer-join", message);
    }
    
    @MessageMapping("/stream/{streamId}/offer")
    public void handleOffer(@DestinationVariable String streamId, @Payload Map<String, Object> message) {
        System.out.println("Offer received for stream: " + streamId);
        String viewerId = (String) message.get("viewerId");
        
        // Forward offer to viewer
        messagingTemplate.convertAndSend("/topic/stream/" + streamId + "/viewer/" + viewerId + "/offer", message);
    }
    
    @MessageMapping("/stream/{streamId}/answer")
    public void handleAnswer(@DestinationVariable String streamId, @Payload Map<String, Object> message) {
        System.out.println("Answer received for stream: " + streamId);
        String viewerId = (String) message.get("viewerId");
        
        // Forward answer to streamer
        messagingTemplate.convertAndSend("/topic/stream/" + streamId + "/streamer/answer", message);
    }
    
    @MessageMapping("/stream/{streamId}/ice-candidate")
    public void handleIceCandidate(@DestinationVariable String streamId, @Payload Map<String, Object> message) {
        System.out.println("ICE candidate received for stream: " + streamId);
        String viewerId = (String) message.get("viewerId");
        String target = (String) message.get("target");
        
        if ("viewer".equals(target)) {
            // Forward to viewer
            messagingTemplate.convertAndSend("/topic/stream/" + streamId + "/viewer/" + viewerId + "/ice-candidate", message);
        } else {
            // Forward to streamer
            messagingTemplate.convertAndSend("/topic/stream/" + streamId + "/streamer/ice-candidate", message);
        }
    }
    
    @MessageMapping("/stream/{streamId}/streamer-ready")
    public void handleStreamerReady(@DestinationVariable String streamId, @Payload Map<String, Object> message) {
        System.out.println("Streamer ready for stream: " + streamId);
        String streamerId = (String) message.get("streamerId");
        streamerSessions.put(streamId, streamerId);
        
        // Notify viewers that streamer is ready
        messagingTemplate.convertAndSend("/topic/stream/" + streamId + "/streamer-ready", message);
    }
} 