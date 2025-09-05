package goormton.team.gotjob.global.config.security;

import goormton.team.gotjob.domain.chat.ws.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 프런트는 ws://<host>/ws 로 연결 (SockJS 사용)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // 필요 없으면 .withSockJS() 제거
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 서버→클라 브로드캐스트 prefix
        registry.enableSimpleBroker("/topic");
        // 클라→서버 전송 prefix
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler); // JWT 검사
    }
}