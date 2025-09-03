package goormton.team.gotjob.domain.consultant.controller;

import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.consultant.dto.ConsultantMatchCreateRequest;
import goormton.team.gotjob.domain.consultant.dto.ConsultantMatchResponse;
import goormton.team.gotjob.domain.consultant.dto.ConsultantMatchUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultants/matches")
public class ConsultantMatchController {

    @PostMapping
    public ApiResponse<ConsultantMatchResponse> create(@RequestBody ConsultantMatchCreateRequest req) {
        return ApiResponse.ok(new ConsultantMatchResponse(1L, req.consultantId(), req.userId(),
                "ACTIVE", req.note(), "2025-09-01"));
    }

    @GetMapping
    public ApiResponse<List<ConsultantMatchResponse>> list(@RequestParam(required = false) Long consultantId,
                                                           @RequestParam(required = false) Long userId) {
        return ApiResponse.ok(List.of(new ConsultantMatchResponse(1L, 2L, 1L,
                "ACTIVE", "매칭 노트", "2025-09-01")));
    }

    @PatchMapping("/{id}")
    public ApiResponse<ConsultantMatchResponse> update(@PathVariable Long id,
                                                       @RequestBody ConsultantMatchUpdateRequest req) {
        return ApiResponse.ok(new ConsultantMatchResponse(id, 2L, 1L,
                req.status(), "매칭 노트", "2025-09-01"));
    }
}