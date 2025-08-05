package com.WebSiteChoiGame.DoAnJ2EE.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketController extends TextWebSocketHandler {
    
    private static final Map<String, WebSocketSession> streamerSessions = new ConcurrentHashMap<>();
    private static final Map<String, WebSocketSession> viewerSessions = new ConcurrentHashMap<>();
    
    public WebSocketController() {
        System.out.println("=== WebSocketController initialized ===");
    }
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = session.getUri().getPath();
        System.out.println("WebSocket connected: " + path);
        
        // Extract streamId and role from path: /ws/signaling/{streamId}/{role}
        String[] pathParts = path.split("/");
        if (pathParts.length >= 5) {
            String streamId = pathParts[3];
            String role = pathParts[4];
            
            if ("streamer".equals(role)) {
                streamerSessions.put(streamId, session);
                System.out.println("Streamer connected for stream: " + streamId);
            } else if ("viewer".equals(role)) {
                viewerSessions.put(streamId, session);
                System.out.println("Viewer connected for stream: " + streamId);
            }
        }
    }


    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);
        
        String path = session.getUri().getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length >= 5) {
            String streamId = pathParts[3];
            String role = pathParts[4];
            
            if ("viewer".equals(role)) {
                // Message from viewer, forward to streamer
                WebSocketSession streamerSession = streamerSessions.get(streamId);
                if (streamerSession != null && streamerSession.isOpen()) {
                    System.out.println("Forwarding message from viewer to streamer for stream: " + streamId);
                    streamerSession.sendMessage(message);
                } else {
                    System.out.println("No streamer session found for stream: " + streamId);
                }
            } else if ("streamer".equals(role)) {
                // Message from streamer, forward to viewer
                WebSocketSession viewerSession = viewerSessions.get(streamId);
                if (viewerSession != null && viewerSession.isOpen()) {
                    System.out.println("Forwarding message from streamer to viewer for stream: " + streamId);
                    viewerSession.sendMessage(message);
                } else {
                    System.out.println("No viewer session found for stream: " + streamId);
                }
            }
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        String path = session.getUri().getPath();
        System.out.println("WebSocket disconnected: " + path + " with status: " + status);
        
        String[] pathParts = path.split("/");
        if (pathParts.length >= 5) {
            String streamId = pathParts[3];
            String role = pathParts[4];
            
            if ("streamer".equals(role)) {
                streamerSessions.remove(streamId);
                System.out.println("Streamer session removed for stream: " + streamId);
            } else if ("viewer".equals(role)) {
                viewerSessions.remove(streamId);
                System.out.println("Viewer session removed for stream: " + streamId);
            }
        }
    }
} 