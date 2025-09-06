package goormton.team.gotjob.domain.company.controller;


import goormton.team.gotjob.domain.company.dto.CompanyCreateRequest;
import goormton.team.gotjob.domain.company.dto.CompanyResponse;
import goormton.team.gotjob.domain.company.dto.CompanyUpdateRequest;
import goormton.team.gotjob.domain.company.service.CompanyService;
import goormton.team.gotjob.domain.common.ApiResponse;
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

    @PreAuthorize("hasRole('COMPANY')")
    @PostMapping
    public ApiResponse<CompanyResponse> create(@RequestBody CompanyCreateRequest req,
                                               @AuthenticationPrincipal CustomUserDetails me) {
        return ApiResponse.ok(svc.create(me.id(), req));
    }

    @GetMapping("/{id}")
    public ApiResponse<CompanyResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(svc.get(id));
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PutMapping("/{id}")
    public ApiResponse<CompanyResponse> update(@PathVariable Long id,
                                               @RequestBody CompanyUpdateRequest req,
                                               @AuthenticationPrincipal CustomUserDetails me) {
        return ApiResponse.ok(svc.update(id, req, me.id()));
    }
}
