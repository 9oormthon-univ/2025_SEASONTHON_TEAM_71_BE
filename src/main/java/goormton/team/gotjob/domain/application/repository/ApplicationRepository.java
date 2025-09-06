package goormton.team.gotjob.domain.application.repository;

import goormton.team.gotjob.domain.application.domain.Application;
import goormton.team.gotjob.domain.job.domain.Job;
import goormton.team.gotjob.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
}