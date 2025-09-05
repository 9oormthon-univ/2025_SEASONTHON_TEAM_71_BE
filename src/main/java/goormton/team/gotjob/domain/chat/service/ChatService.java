package goormton.team.gotjob.domain.chat.service;


import goormton.team.gotjob.domain.chat.domain.ChatMessage;
import goormton.team.gotjob.domain.chat.domain.ChatRoom;
import goormton.team.gotjob.domain.chat.dto.ChatMessageResponse;
import goormton.team.gotjob.domain.chat.dto.ChatMessageSendRequest;
import goormton.team.gotjob.domain.chat.dto.ChatRoomCreateRequest;
import goormton.team.gotjob.domain.chat.dto.ChatRoomResponse;
import goormton.team.gotjob.domain.chat.repository.ChatMessageRepository;
import goormton.team.gotjob.domain.chat.repository.ChatRoomRepository;
import goormton.team.gotjob.domain.common.ApiException;
import goormton.team.gotjob.domain.user.domain.User;
import goormton.team.gotjob.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service @RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository rooms;
    private final ChatMessageRepository messages;
    private final UserRepository users;

    @Transactional
    public ChatRoomResponse createRoom(ChatRoomCreateRequest req){
        User c = users.findById(req.consultantId()).orElseThrow(()->new ApiException(404,"consultant not found"));
        User u = users.findById(req.userId()).orElseThrow(()->new ApiException(404,"user not found"));
        var existing = rooms.findByConsultantAndUser(c,u);
        ChatRoom r = existing.orElseGet(()->rooms.save(ChatRoom.builder().consultant(c).user(u).build()));
        return new ChatRoomResponse(r.getId(), c.getId(), u.getId(), r.getCreatedAt()!=null? r.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME):null);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> rooms(Long userId){
        return rooms.findByConsultantIdOrUserId(userId, userId).stream()
                .map(r -> new ChatRoomResponse(r.getId(), r.getConsultant()!=null?r.getConsultant().getId():null,
                        r.getUser()!=null?r.getUser().getId():null, r.getCreatedAt()!=null? r.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME):null))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> history(Long roomId){
        return messages.findByRoomIdOrderByIdAsc(roomId).stream()
                .map(m -> new ChatMessageResponse(m.getId(), m.getRoom().getId(),
                        m.getSender()!=null?m.getSender().getId():null, m.getContent(),
                        m.getCreatedAt()!=null? m.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME):null,
                        m.getReadAt()!=null? m.getReadAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME):null))
                .toList();
    }

    @Transactional
    public ChatMessageResponse send(Long roomId, ChatMessageSendRequest req){
        ChatRoom r = rooms.findById(roomId).orElseThrow(()->new ApiException(404,"room not found"));
        User s = users.findById(req.senderUserId()).orElseThrow(()->new ApiException(404,"sender not found"));
        ChatMessage m = messages.save(ChatMessage.builder().room(r).sender(s).content(req.content()).build());
        return new ChatMessageResponse(m.getId(), r.getId(), s.getId(), m.getContent(),
                m.getCreatedAt()!=null? m.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME):null, null);
    }
}
