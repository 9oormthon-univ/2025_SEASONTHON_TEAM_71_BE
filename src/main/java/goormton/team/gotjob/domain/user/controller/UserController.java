package goormton.team.gotjob.domain.user.controller;

import goormton.team.gotjob.domain.user.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.user.service.AuthService;
import goormton.team.gotjob.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final AuthService auth;
    private final UserService usersvc;

    @PostMapping("/auth/signup")
    public ApiResponse<SignupResponse> signup(@RequestBody SignupRequest req){ return ApiResponse.ok(auth.signup(req)); }

    @PostMapping("/auth/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest req){ return ApiResponse.ok(auth.login(req)); }

    @GetMapping("/users/me")
    public ApiResponse<MeResponse> me(){ return ApiResponse.ok(usersvc.me(1L)); } // TODO JWT 붙이면 교체

    @PutMapping("/users/me")
    public ApiResponse<MeResponse> updateMe(@RequestBody UpdateMeRequest req){ return ApiResponse.ok(usersvc.update(1L, req)); }

}