package goormton.team.gotjob.domain.user.controller;

import goormton.team.gotjob.domain.user.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @PostMapping("/auth/signup")
    public ApiResponse<SignupResponse> signup(@RequestBody SignupRequest req) {
        return ApiResponse.ok(new SignupResponse(1L, req.username(), req.email(), req.role()));
    }

    @PostMapping("/auth/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest req) {
        return ApiResponse.ok(new TokenResponse("dummy-jwt-token", "Bearer", 3600));
    }

    @GetMapping("/users/me")
    public ApiResponse<MeResponse> me() {
        return ApiResponse.ok(new MeResponse(
                1L, "chahyeon", "me@example.com", "USER",
                "백엔드 개발자", 2, "Seoul", "resume.pdf", "portfolio.com"));
    }

    @PutMapping("/users/me")
    public ApiResponse<MeResponse> updateMe(@RequestBody UpdateMeRequest req) {
        return ApiResponse.ok(new MeResponse(
                1L, "chahyeon", "me@example.com", "USER",
                req.bio(), req.yearsExperience(), req.location(),
                req.resumeUrl(), req.portfolioUrl()));
    }
}