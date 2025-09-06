package goormton.team.gotjob.domain.consultant.controller;

import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.consultant.dto.KeywordMatchDto;
import goormton.team.gotjob.domain.consultant.service.KeywordMatchingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/consultants/matches")
@RequiredArgsConstructor
public class ConsultantMatchController {
    private final KeywordMatchingService kms;

    @Operation(summary = "키워드 매칭 api", description = "fileId와 함께 호출한다면, 이미 매칭이 된 컨설턴트를 제외하고 키워드 기반 유사도 매칭 알고리즘을 통해 컨설턴트를 매칭")
    @GetMapping("/{fileId}")
    public ApiResponse<List<KeywordMatchDto>> getMatching(@PathVariable Long fileId) {
        List<KeywordMatchDto> matches = kms.findMatchingConsultants(fileId);
        return ApiResponse.ok(matches);
    }

}