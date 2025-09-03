package goormton.team.gotjob.domain.chat.dto;

public record ChatMessageSendRequest(Long senderUserId, String content, String clientMessageId) {}

