package goormton.team.gotjob.domain.job.controller;

import goormton.team.gotjob.domain.job.dto.*;
import goormton.team.gotjob.domain.common.*;
import goormton.team.gotjob.domain.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService svc;

    @PreAuthorize("hasAnyRole('COMPANY','ADMIN')")
    @PostMapping
    public ApiResponse<JobResponse> create(@RequestBody JobCreateRequest req){
        return ApiResponse.ok(svc.create(req));
    }

    @GetMapping("/{id}")
    public ApiResponse<JobResponse> get(@PathVariable Long id){
        return ApiResponse.ok(svc.get(id));
    }

    @PreAuthorize("hasAnyRole('COMPANY','ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<JobResponse> update(@PathVariable Long id, @RequestBody JobUpdateRequest req){
        return ApiResponse.ok(svc.update(id, req));
    }

    @GetMapping
    public ApiResponse<PagedResponse<JobResponse>> search(JobSearchParams params){
        return ApiResponse.ok(svc.search(params));
    }
}