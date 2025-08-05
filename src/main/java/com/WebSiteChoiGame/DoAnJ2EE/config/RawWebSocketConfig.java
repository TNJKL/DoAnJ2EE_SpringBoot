package com.WebSiteChoiGame.DoAnJ2EE.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.WebSiteChoiGame.DoAnJ2EE.controller.WebSocketController;

@Configuration
@EnableWebSocket
public class RawWebSocketConfig implements WebSocketConfigurer {

    private final WebSocketController webSocketController;

    public RawWebSocketConfig(WebSocketController webSocketController) {
        this.webSocketController = webSocketController;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketController, "/ws/signaling/{streamId}/{role}")
                .setAllowedOriginPatterns("*");
    }
} 