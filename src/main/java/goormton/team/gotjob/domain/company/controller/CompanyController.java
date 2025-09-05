package goormton.team.gotjob.domain.company.controller;

import goormton.team.gotjob.domain.company.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.company.service.CompanyService;
import goormton.team.gotjob.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService svc;

    // 기업/관리자만 생성
    @PreAuthorize("hasAnyRole('COMPANY','ADMIN')")
    @PostMapping
    public ApiResponse<CompanyResponse> create(@RequestBody CompanyCreateRequest req,
                                               @AuthenticationPrincipal CustomUserDetails me){
        return ApiResponse.ok(svc.create(me.id(), req));
    }

    // 조회는 공개
    @GetMapping("/{id}")
    public ApiResponse<CompanyResponse> get(@PathVariable Long id){
        return ApiResponse.ok(svc.get(id));
    }

    // 기업/관리자만 수정
    @PreAuthorize("hasAnyRole('COMPANY','ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<CompanyResponse> update(@PathVariable Long id,
                                               @RequestBody CompanyUpdateRequest req){
        return ApiResponse.ok(svc.update(id, req));
    }
}