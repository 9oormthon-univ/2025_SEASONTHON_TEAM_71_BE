package goormton.team.gotjob.domain.user.repository;

import goormton.team.gotjob.domain.user.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {}