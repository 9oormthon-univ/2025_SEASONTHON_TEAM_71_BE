package goormton.team.gotjob.domain.consultant.controller;

import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.consultant.dto.ConsultantMatchCreateRequest;
import goormton.team.gotjob.domain.consultant.dto.ConsultantMatchResponse;
import goormton.team.gotjob.domain.consultant.dto.ConsultantMatchUpdateRequest;
import goormton.team.gotjob.domain.consultant.dto.KeywordMatchDto;
import goormton.team.gotjob.domain.consultant.service.ConsultantMatchService;
import goormton.team.gotjob.domain.consultant.service.KeywordMatchingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultants/matches")
@RequiredArgsConstructor
public class ConsultantMatchController {
    private final ConsultantMatchService svc;
    private final KeywordMatchingService kms;

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

    @Operation(summary = "키워드 매칭 api", description = "fileId와 함께 호출한다면, 이미 매칭이 된 컨설턴트를 제외하고 키워드 기반 유사도 매칭 알고리즘을 통해 컨설턴트를 매칭")
    @GetMapping("/{fileId}")
    public ApiResponse<List<KeywordMatchDto>> getMatching(@PathVariable Long fileId) {
        List<KeywordMatchDto> matches = kms.findMatchingConsultants(fileId);
        return ApiResponse.ok(matches);
    }

}