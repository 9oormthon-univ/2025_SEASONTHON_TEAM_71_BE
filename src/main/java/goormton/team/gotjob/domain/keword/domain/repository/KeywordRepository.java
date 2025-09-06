package goormton.team.gotjob.domain.keword.domain.repository;

import goormton.team.gotjob.domain.keword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByFileId(Long fileId);
}
