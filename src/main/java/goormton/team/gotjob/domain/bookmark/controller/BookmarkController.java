package goormton.team.gotjob.domain.bookmark.controller;

import goormton.team.gotjob.domain.bookmark.dto.BookmarkResponse;
import goormton.team.gotjob.domain.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookmarkController {

    @PostMapping("/jobs/{id}/bookmark")
    public ApiResponse<String> add(@PathVariable Long id) {
        return ApiResponse.ok("Bookmarked job " + id);
    }

    @DeleteMapping("/jobs/{id}/bookmark")
    public ApiResponse<String> remove(@PathVariable Long id) {
        return ApiResponse.ok("Unbookmarked job " + id);
    }

    @GetMapping("/bookmarks/me")
    public ApiResponse<List<BookmarkResponse>> myBookmarks() {
        return ApiResponse.ok(List.of(new BookmarkResponse(1L, "백엔드 개발자", "네이버", "2025-09-01")));
    }
}