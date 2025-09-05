package goormton.team.gotjob.domain.chat.controller;

import goormton.team.gotjob.domain.chat.dto.ChatMessageSendRequest;
import goormton.team.gotjob.domain.chat.dto.ChatMessageResponse;
import goormton.team.gotjob.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messaging;

    @MessageMapping("/chat/{roomId}")
    public void send(@DestinationVariable Long roomId,
                     ChatMessageSendRequest payload) {
        // DB 저장 + 검증은 ChatService에서 수행
        ChatMessageResponse saved = chatService.send(roomId, payload);

        // 구독자에게 push
        messaging.convertAndSend("/topic/chat/" + roomId, saved);
    }
}
