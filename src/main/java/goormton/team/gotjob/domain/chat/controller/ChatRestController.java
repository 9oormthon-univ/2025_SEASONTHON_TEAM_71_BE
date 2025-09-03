package goormton.team.gotjob.domain.chat.controller;


import goormton.team.gotjob.domain.chat.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @PostMapping("/rooms")
    public ApiResponse<ChatRoomResponse> createRoom(@RequestBody ChatRoomCreateRequest req) {
        return ApiResponse.ok(new ChatRoomResponse(1L, req.consultantId(), req.userId(), "2025-09-01"));
    }

    @GetMapping("/rooms")
    public ApiResponse<List<ChatRoomResponse>> myRooms(@RequestParam Long userId) {
        return ApiResponse.ok(List.of(new ChatRoomResponse(1L, 2L, userId, "2025-09-01")));
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ApiResponse<List<ChatMessageResponse>> history(@PathVariable Long roomId) {
        return ApiResponse.ok(List.of(new ChatMessageResponse(1L, roomId, 1L,
                "안녕하세요", "2025-09-01", null)));
    }

    @PostMapping("/rooms/{roomId}/messages")
    public ApiResponse<ChatMessageResponse> sendMessage(@PathVariable Long roomId,
                                                        @RequestBody ChatMessageSendRequest req) {
        return ApiResponse.ok(new ChatMessageResponse(2L, roomId, req.senderUserId(),
                req.content(), "2025-09-01", null));
    }
}