package goormton.team.gotjob.domain.user.controller;

import goormton.team.gotjob.domain.user.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.user.service.UserService;
import goormton.team.gotjob.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService usersvc;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/me")
    public ApiResponse<MeResponse> me(@AuthenticationPrincipal CustomUserDetails me){
        return ApiResponse.ok(usersvc.me(me.id()));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/users/me")
    public ApiResponse<MeResponse> updateMe(@RequestBody UpdateMeRequest req,
                                            @AuthenticationPrincipal CustomUserDetails me){
        return ApiResponse.ok(usersvc.update(me.id(), req));
    }
}