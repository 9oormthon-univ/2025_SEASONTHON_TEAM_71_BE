package goormton.team.gotjob.domain.application.controller;

import goormton.team.gotjob.domain.application.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    @PostMapping("/jobs/{id}/apply")
    public ApiResponse<ApplicationResponse> apply(@PathVariable Long id, @RequestBody ApplyRequest req) {
        return ApiResponse.ok(new ApplicationResponse(1L, 1L, "차현",
                id, "백엔드 개발자", "네이버", "APPLIED", "2025-09-01"));
    }

    @GetMapping("/applications/me")
    public ApiResponse<List<ApplicationResponse>> myApplications() {
        return ApiResponse.ok(List.of(new ApplicationResponse(1L, 1L, "차현",
                1L, "백엔드 개발자", "네이버", "APPLIED", "2025-09-01")));
    }

    @GetMapping("/jobs/{id}/applications")
    public ApiResponse<List<ApplicationResponse>> listApplicants(@PathVariable Long id) {
        return ApiResponse.ok(List.of(
                new ApplicationResponse(11L, 101L, "지원자A", id, "백엔드", "네이버", "REVIEWING", "2025-09-01"),
                new ApplicationResponse(12L, 102L, "지원자B", id, "백엔드", "네이버", "APPLIED", "2025-09-02")
        ));
    }

    @PatchMapping("/applications/{id}")
    public ApiResponse<ApplicationResponse> updateStatus(@PathVariable Long id,
                                                         @RequestBody ApplicationStatusUpdateRequest req) {
        return ApiResponse.ok(new ApplicationResponse(id, 1L, "차현",
                1L, "백엔드 개발자", "네이버", req.status(), "2025-09-01"));
    }
}