package goormton.team.gotjob.domain.company.controller;

import goormton.team.gotjob.domain.company.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @PostMapping
    public ApiResponse<CompanyResponse> create(@RequestBody CompanyCreateRequest req) {
        return ApiResponse.ok(new CompanyResponse(1L, req.name(), req.businessNo(),
                req.description(), req.size(), req.website(), req.address(), 1L));
    }

    @GetMapping("/{id}")
    public ApiResponse<CompanyResponse> get(@PathVariable Long id) {
        return ApiResponse.ok(new CompanyResponse(id, "네이버", "123-45-67890",
                "검색 포털", "L", "https://naver.com", "Seoul", 1L));
    }

    @PutMapping("/{id}")
    public ApiResponse<CompanyResponse> update(@PathVariable Long id,
                                               @RequestBody CompanyUpdateRequest req) {
        return ApiResponse.ok(new CompanyResponse(id, req.name(), req.businessNo(),
                req.description(), req.size(), req.website(), req.address(), 1L));
    }
}