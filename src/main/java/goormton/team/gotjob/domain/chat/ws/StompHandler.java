package goormton.team.gotjob.domain.chat.ws;

import goormton.team.gotjob.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider tokens;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor acc = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(acc.getCommand())
                || StompCommand.SUBSCRIBE.equals(acc.getCommand())
                || StompCommand.SEND.equals(acc.getCommand())) {

            String auth = acc.getFirstNativeHeader("Authorization"); // "Bearer xxx"
            if (!StringUtils.hasText(auth) || !auth.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing Authorization header");
            }
            String token = auth.substring(7);
            // 유효하지 않으면 예외 발생 → 연결/전송 차단
            Long userId = tokens.getUserId(token);

            // 세션에 userId 저장 (필요시 사용)
            acc.getSessionAttributes().put("userId", userId);
        }
        return message;
    }
}