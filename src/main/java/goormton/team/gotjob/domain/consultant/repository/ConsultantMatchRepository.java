package goormton.team.gotjob.domain.consultant.repository;

import goormton.team.gotjob.domain.consultant.domain.ConsultantMatch;
import goormton.team.gotjob.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultantMatchRepository extends JpaRepository<ConsultantMatch, Long> {
    Optional<ConsultantMatch> findByConsultantAndUser(User consultant, User user);
    List<ConsultantMatch> findByConsultantId(Long consultantId);
    List<ConsultantMatch> findByUserId(Long userId);
}