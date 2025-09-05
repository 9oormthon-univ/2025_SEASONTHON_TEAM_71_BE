package goormton.team.gotjob.domain.bookmark.repository;

import goormton.team.gotjob.domain.bookmark.domain.Bookmark;
import goormton.team.gotjob.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserIdAndJobId(Long userId, Long jobId);
    List<Bookmark> findByUser(User user);
}
