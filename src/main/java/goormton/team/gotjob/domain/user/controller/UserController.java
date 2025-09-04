package goormton.team.gotjob.domain.user.controller;

import goormton.team.gotjob.domain.user.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.user.service.AuthService;
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
    private final AuthService auth;
    private final UserService userService;

    @PostMapping("/auth/signup")
    public ApiResponse<SignupResponse> signup(@RequestBody SignupRequest req){ return ApiResponse.ok(auth.signup(req)); }

    @PostMapping("/auth/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest req){ return ApiResponse.ok(auth.login(req)); }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ApiResponse<MeResponse> me(@AuthenticationPrincipal CustomUserDetails me){
        return ApiResponse.ok(userService.me(me.id()));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me")
    public ApiResponse<MeResponse> updateMe(@RequestBody UpdateMeRequest req,
                                            @AuthenticationPrincipal CustomUserDetails me){
        return ApiResponse.ok(userService.update(me.id(), req));
    }

}