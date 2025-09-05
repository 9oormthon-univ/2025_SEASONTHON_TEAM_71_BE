package goormton.team.gotjob.domain.application.controller;

import goormton.team.gotjob.domain.application.dto.*;
import goormton.team.gotjob.domain.application.service.ApplicationService;
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
public class ApplicationController {
    private final ApplicationService svc;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/jobs/{id}/apply")
    public ApiResponse<ApplicationResponse> apply(@PathVariable Long id,
                                                  @RequestBody ApplyRequest req,
                                                  @AuthenticationPrincipal CustomUserDetails me){
        return ApiResponse.ok(svc.apply(me.id(), id, req));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/applications/me")
    public ApiResponse<List<ApplicationResponse>> myApplications(@AuthenticationPrincipal CustomUserDetails me){
        return ApiResponse.ok(svc.myApplications(me.id()));
    }

    // 기업/관리자만 지원자 목록 조회
    @PreAuthorize("hasAnyRole('COMPANY','ADMIN')")
    @GetMapping("/jobs/{id}/applications")
    public ApiResponse<List<ApplicationResponse>> applicants(@PathVariable Long id){
        return ApiResponse.ok(svc.applicants(id));
    }

    // 기업/관리자만 지원 상태 변경
    @PreAuthorize("hasAnyRole('COMPANY','ADMIN')")
    @PatchMapping("/applications/{id}")
    public ApiResponse<ApplicationResponse> updateStatus(@PathVariable Long id,
                                                         @RequestBody ApplicationStatusUpdateRequest req){
        return ApiResponse.ok(svc.updateStatus(id, req));
    }
}