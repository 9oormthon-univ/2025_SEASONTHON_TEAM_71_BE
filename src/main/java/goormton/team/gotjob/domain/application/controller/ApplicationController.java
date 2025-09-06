package goormton.team.gotjob.domain.application.controller;

import goormton.team.gotjob.domain.application.dto.ApplyRequest;
import goormton.team.gotjob.domain.application.dto.ApplicationResponse;
import goormton.team.gotjob.domain.application.service.ApplicationService;
import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService svc;

    @PreAuthorize("hasRole('PERSONAL')")
    @PostMapping("/jobs/{id}/apply")
    public ApiResponse<ApplicationResponse> apply(@PathVariable("id") Long jobId,
                                                  @RequestBody ApplyRequest req,
                                                  @AuthenticationPrincipal CustomUserDetails me) {
        return ApiResponse.ok(svc.apply(me.id(), jobId, req));
    }
}
