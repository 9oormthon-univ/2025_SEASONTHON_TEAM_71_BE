package goormton.team.gotjob.domain.user.controller;

import goormton.team.gotjob.domain.user.dto.*;
import goormton.team.gotjob.domain.user.service.AuthService;
import goormton.team.gotjob.domain.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;

    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(@RequestBody SignupRequest req){
        return ApiResponse.ok(auth.signup(req));
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest req){
        return ApiResponse.ok(auth.login(req));
    }
}