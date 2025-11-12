package com.ateam.calmate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독(prefix): 클라이언트가 subscribe하는 경로
        registry.enableSimpleBroker("/topic", "/queue");
        // 발행(prefix): 클라이언트가 send할 때 사용하는 prefix
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-gacha")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SockJS fallback(필요 없으면 제거 가능)
    }
}