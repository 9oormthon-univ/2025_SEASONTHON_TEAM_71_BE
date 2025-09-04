package goormton.team.gotjob.domain.company.controller;

import goormton.team.gotjob.domain.company.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService svc;

    @PostMapping
    public ApiResponse<CompanyResponse> create(@RequestBody CompanyCreateRequest req){ return ApiResponse.ok(svc.create(1L, req)); }

    @GetMapping("/{id}")
    public ApiResponse<CompanyResponse> get(@PathVariable Long id){ return ApiResponse.ok(svc.get(id)); }

    @PutMapping("/{id}")
    public ApiResponse<CompanyResponse> update(@PathVariable Long id, @RequestBody CompanyUpdateRequest req){ return ApiResponse.ok(svc.update(id, req)); }

}