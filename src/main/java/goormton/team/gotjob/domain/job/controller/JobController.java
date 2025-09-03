package goormton.team.gotjob.domain.job.controller;

import goormton.team.gotjob.domain.job.dto.*;
import goormton.team.gotjob.domain.common.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @PostMapping
    public ApiResponse<JobResponse> create(@RequestBody JobCreateRequest req) {
        return ApiResponse.ok(new JobResponse(1L, req.title(), req.employmentType(), req.location(),
                req.minSalary(), req.maxSalary(), req.requirements(), req.description(),
                "OPEN", req.deadline(), new CompanySummary(req.companyId(), "네이버")));
    }

    @GetMapping("/{id}")
    public ApiResponse<JobResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(new JobResponse(id, "백엔드 개발자", "FULLTIME", "Seoul",
                3000, 5000, "Java, Spring", "설명입니다", "OPEN", "2025-12-31",
                new CompanySummary(1L, "네이버")));
    }

    @PutMapping("/{id}")
    public ApiResponse<JobResponse> update(@PathVariable Long id, @RequestBody JobUpdateRequest req) {
        return ApiResponse.ok(new JobResponse(id, req.title(), req.employmentType(), req.location(),
                req.minSalary(), req.maxSalary(), req.requirements(), req.description(),
                req.status(), req.deadline(), new CompanySummary(1L, "네이버")));
    }

    @GetMapping
    public ApiResponse<PagedResponse<JobResponse>> search(JobSearchParams params) {
        JobResponse job = new JobResponse(1L, "백엔드 개발자", "FULLTIME", "Seoul",
                3000, 5000, "Java, Spring", "설명입니다", "OPEN", "2025-12-31",
                new CompanySummary(1L, "네이버"));
        return ApiResponse.ok(new PagedResponse<>(List.of(job), 0, 10, 1));
    }
}