package goormton.team.gotjob.domain.consultant.domain.repository;

import goormton.team.gotjob.domain.consultant.domain.ConsultantMatch;
import goormton.team.gotjob.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ConsultantMatchRepository extends JpaRepository<ConsultantMatch, Long> {

    // 특정 상태(status)에 있는 모든 컨설턴트의 User 객체를 조회.
    @Query("SELECT cm.consultant FROM ConsultantMatch cm WHERE cm.matchStatus = :status")
    Set<User> findConsultantsByMatchStatus(@Param("status") String status);
}
