package goormton.team.gotjob.domain.chat.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="chat_messages")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessage extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="room_id", nullable=false)
    private ChatRoom room;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="sender_user_id")
    private User sender;

    @Lob private String content;
    private LocalDateTime readAt;
}
