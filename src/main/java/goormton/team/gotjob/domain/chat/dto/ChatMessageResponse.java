package goormton.team.gotjob.domain.chat.dto;

public record ChatMessageResponse(Long id, Long roomId, Long senderUserId,
                                  String content, String createdAt, String readAt) {}