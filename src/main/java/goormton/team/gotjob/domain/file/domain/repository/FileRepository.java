package goormton.team.gotjob.domain.file.domain.repository;

import goormton.team.gotjob.domain.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByUserId(Long userId);
}
