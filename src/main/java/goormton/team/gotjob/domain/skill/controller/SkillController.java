package goormton.team.gotjob.domain.skill.controller;

import goormton.team.gotjob.domain.skill.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @GetMapping
    public ApiResponse<List<SkillResponse>> list() {
        return ApiResponse.ok(List.of(
                new SkillResponse(1L, "Java"),
                new SkillResponse(2L, "Spring")));
    }

    @PostMapping
    public ApiResponse<SkillResponse> create(@RequestBody SkillCreateRequest req) {
        return ApiResponse.ok(new SkillResponse(3L, req.name()));
    }

    @PostMapping("/link")
    public ApiResponse<SkillLinkResponse> link(@RequestBody SkillLinkRequest req) {
        return ApiResponse.ok(new SkillLinkResponse(1L, req.skillId(), "Java",
                req.targetType(), req.targetId(), req.level(), req.required(), req.weight()));
    }

    @DeleteMapping("/link/{id}")
    public ApiResponse<String> unlink(@PathVariable Long id) {
        return ApiResponse.ok("Unlinked skill link " + id);
    }
}