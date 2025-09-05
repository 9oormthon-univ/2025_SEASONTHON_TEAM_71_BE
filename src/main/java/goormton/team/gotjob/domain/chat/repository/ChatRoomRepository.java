package goormton.team.gotjob.domain.chat.repository;

import goormton.team.gotjob.domain.chat.domain.ChatRoom;
import goormton.team.gotjob.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByConsultantAndUser(User consultant, User user);
    List<ChatRoom> findByConsultantIdOrUserId(Long consultantId, Long userId);
}