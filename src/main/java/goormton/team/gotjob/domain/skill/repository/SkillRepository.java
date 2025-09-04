package goormton.team.gotjob.domain.skill.repository;

import goormton.team.gotjob.domain.skill.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    boolean existsByName(String name);
    Optional<Skill> findByName(String name);
}
