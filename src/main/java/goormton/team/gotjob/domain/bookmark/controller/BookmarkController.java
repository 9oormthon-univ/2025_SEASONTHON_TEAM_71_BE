package goormton.team.gotjob.domain.bookmark.controller;

import goormton.team.gotjob.domain.bookmark.dto.BookmarkResponse;
import goormton.team.gotjob.domain.bookmark.service.BookmarkService;
import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService svc;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/jobs/{id}/bookmark")
    public ApiResponse<String> add(@PathVariable Long id,
                                   @AuthenticationPrincipal CustomUserDetails me){
        svc.add(me.id(), id);
        return ApiResponse.ok("Bookmarked job " + id);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/jobs/{id}/bookmark")
    public ApiResponse<String> remove(@PathVariable Long id,
                                      @AuthenticationPrincipal CustomUserDetails me){
        svc.remove(me.id(), id);
        return ApiResponse.ok("Unbookmarked job " + id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/bookmarks/me")
    public ApiResponse<List<BookmarkResponse>> myBookmarks(@AuthenticationPrincipal CustomUserDetails me){
        return ApiResponse.ok(svc.myList(me.id()));
    }
}