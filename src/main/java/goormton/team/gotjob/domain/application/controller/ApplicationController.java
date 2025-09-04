package goormton.team.gotjob.domain.application.controller;

import goormton.team.gotjob.domain.application.dto.*;
import goormton.team.gotjob.domain.application.service.ApplicationService;
import goormton.team.gotjob.domain.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService svc;

    @PostMapping("/jobs/{id}/apply")
    public ApiResponse<ApplicationResponse> apply(@PathVariable Long id, @RequestBody ApplyRequest req){
        return ApiResponse.ok(svc.apply(1L, id, req)); // 임시 userId=1
    }

    @GetMapping("/applications/me")
    public ApiResponse<List<ApplicationResponse>> myApplications(){ return ApiResponse.ok(svc.myApplications(1L)); }

    @GetMapping("/jobs/{id}/applications")
    public ApiResponse<List<ApplicationResponse>> applicants(@PathVariable Long id){ return ApiResponse.ok(svc.applicants(id)); }

    @PatchMapping("/applications/{id}")
    public ApiResponse<ApplicationResponse> updateStatus(@PathVariable Long id, @RequestBody ApplicationStatusUpdateRequest req){
        return ApiResponse.ok(svc.updateStatus(id, req));
    }
}