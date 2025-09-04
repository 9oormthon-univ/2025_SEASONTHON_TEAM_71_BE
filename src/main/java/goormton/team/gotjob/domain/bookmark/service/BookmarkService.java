package goormton.team.gotjob.domain.bookmark.service;

import goormton.team.gotjob.domain.bookmark.dto.BookmarkResponse;
import goormton.team.gotjob.domain.bookmark.domain.Bookmark;
import goormton.team.gotjob.domain.bookmark.repository.BookmarkRepository;
import goormton.team.gotjob.domain.job.repository.JobRepository;
import goormton.team.gotjob.domain.user.repository.UserRepository;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarks;
    private final UserRepository users;
    private final JobRepository jobs;

    @Transactional
    public void add(Long userId, Long jobId){
        var u = users.findById(userId).orElseThrow(()->new ApiException(404,"user not found"));
        var j = jobs.findById(jobId).orElseThrow(()->new ApiException(404,"job not found"));
        bookmarks.findByUserIdAndJobId(userId, jobId).ifPresent(b->{ throw new ApiException(409,"exists"); });
        bookmarks.save(Bookmark.builder().user(u).job(j).build());
    }

    @Transactional
    public void remove(Long userId, Long jobId){
        var b = bookmarks.findByUserIdAndJobId(userId, jobId).orElseThrow(()->new ApiException(404,"not found"));
        bookmarks.delete(b);
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponse> myList(Long userId){
        var u = users.findById(userId).orElseThrow(()->new ApiException(404,"user not found"));
        return bookmarks.findByUser(u).stream()
                .map(b -> new BookmarkResponse(b.getJob().getId(), b.getJob().getTitle(),
                        b.getJob().getCompany().getName(),
                        b.getJob().getCreatedAt()!=null? b.getJob().getCreatedAt().toString():null))
                .toList();
    }
}