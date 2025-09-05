package goormton.team.gotjob.domain.chat.controller;


import goormton.team.gotjob.domain.chat.dto.*;
import goormton.team.gotjob.domain.chat.service.ChatService;
import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {
    private final ChatService chat;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/rooms")
    public ApiResponse<ChatRoomResponse> createRoom(@RequestBody ChatRoomCreateRequest req){
        return ApiResponse.ok(chat.createRoom(req));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rooms")
    public ApiResponse<List<ChatRoomResponse>> myRooms(@AuthenticationPrincipal CustomUserDetails me){
        return ApiResponse.ok(chat.rooms(me.id()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/rooms/{roomId}/messages")
    public ApiResponse<List<ChatMessageResponse>> history(@PathVariable Long roomId){
        return ApiResponse.ok(chat.history(roomId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/rooms/{roomId}/messages")
    public ApiResponse<ChatMessageResponse> send(@PathVariable Long roomId,
                                                 @RequestBody ChatMessageSendRequest req,
                                                 @AuthenticationPrincipal CustomUserDetails me){
        // 보안상 senderUserId를 토큰의 사용자로 강제
        ChatMessageSendRequest safe = new ChatMessageSendRequest(me.id(), req.content(), req.clientMessageId());
        return ApiResponse.ok(chat.send(roomId, safe));
    }
}