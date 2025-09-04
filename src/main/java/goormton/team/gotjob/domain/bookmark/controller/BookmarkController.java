package goormton.team.gotjob.domain.bookmark.controller;

import goormton.team.gotjob.domain.bookmark.dto.BookmarkResponse;
import goormton.team.gotjob.domain.bookmark.service.BookmarkService;
import goormton.team.gotjob.domain.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService svc;

    @PostMapping("/jobs/{id}/bookmark")
    public ApiResponse<String> add(@PathVariable Long id){ svc.add(1L, id); return ApiResponse.ok("Bookmarked job " + id); }

    @DeleteMapping("/jobs/{id}/bookmark")
    public ApiResponse<String> remove(@PathVariable Long id){ svc.remove(1L, id); return ApiResponse.ok("Unbookmarked job " + id); }

    @GetMapping("/bookmarks/me")
    public ApiResponse<List<BookmarkResponse>> myBookmarks(){ return ApiResponse.ok(svc.myList(1L)); }

}