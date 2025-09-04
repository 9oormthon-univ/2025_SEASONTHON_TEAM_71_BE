package goormton.team.gotjob.domain.consultant.controller;

import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.consultant.dto.ConsultantMatchCreateRequest;
import goormton.team.gotjob.domain.consultant.dto.ConsultantMatchResponse;
import goormton.team.gotjob.domain.consultant.dto.ConsultantMatchUpdateRequest;
import goormton.team.gotjob.domain.consultant.service.ConsultantMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultants/matches")
@RequiredArgsConstructor
public class ConsultantMatchController {
    private final ConsultantMatchService svc;

    @PreAuthorize("hasAnyRole('CONSULTANT','ADMIN')")
    @PostMapping
    public ApiResponse<ConsultantMatchResponse> create(@RequestBody ConsultantMatchCreateRequest req){
        return ApiResponse.ok(svc.create(req));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ApiResponse<List<ConsultantMatchResponse>> list(@RequestParam(required=false) Long consultantId,
                                                           @RequestParam(required=false) Long userId){
        return ApiResponse.ok(svc.list(consultantId, userId));
    }

    @PreAuthorize("hasAnyRole('CONSULTANT','ADMIN')")
    @PatchMapping("/{id}")
    public ApiResponse<ConsultantMatchResponse> update(@PathVariable Long id,
                                                       @RequestBody ConsultantMatchUpdateRequest req){
        return ApiResponse.ok(svc.update(id, req));
    }

}