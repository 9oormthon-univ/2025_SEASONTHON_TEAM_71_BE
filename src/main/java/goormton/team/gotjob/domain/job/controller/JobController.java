package goormton.team.gotjob.domain.job.controller;


import goormton.team.gotjob.domain.job.dto.*;
import goormton.team.gotjob.domain.job.service.JobService;
import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.common.PagedResponse;
import goormton.team.gotjob.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService svc;

    @PreAuthorize("hasRole('COMPANY')")
    @PostMapping
    public ApiResponse<JobResponse> create(@RequestBody JobCreateRequest req,
                                           @AuthenticationPrincipal CustomUserDetails me) {
        return ApiResponse.ok(svc.create(me.id(), req));
    }

    @GetMapping("/{id}")
    public ApiResponse<JobResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(svc.get(id));
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PutMapping("/{id}")
    public ApiResponse<JobResponse> update(@PathVariable Long id,
                                           @RequestBody JobUpdateRequest req,
                                           @AuthenticationPrincipal CustomUserDetails me) {
        return ApiResponse.ok(svc.update(id, me.id(), req));
    }

    @GetMapping
    public ApiResponse<PagedResponse<JobResponse>> search(JobSearchParams params) {
        return ApiResponse.ok(svc.search(params));
    }
}
