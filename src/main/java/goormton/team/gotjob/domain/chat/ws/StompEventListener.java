package goormton.team.gotjob.domain.chat.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class StompEventListener {
    @EventListener
    public void handleConnect(SessionConnectEvent e) {
        log.info("STOMP CONNECT: sessionId={}", e.getMessage().getHeaders().get("simpSessionId"));
    }
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent e) {
        log.info("STOMP DISCONNECT: sessionId={}", e.getSessionId());
    }
}
