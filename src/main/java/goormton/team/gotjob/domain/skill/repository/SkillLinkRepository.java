package goormton.team.gotjob.domain.skill.repository;

import goormton.team.gotjob.domain.skill.domain.SkillLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillLinkRepository extends JpaRepository<SkillLink, Long> {
    List<SkillLink> findByTargetTypeAndTargetId(String targetType, Long targetId);
}