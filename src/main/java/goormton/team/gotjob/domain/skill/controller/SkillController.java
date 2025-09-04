package goormton.team.gotjob.domain.skill.controller;

import goormton.team.gotjob.domain.skill.dto.*;
import goormton.team.gotjob.domain.common.ApiResponse;
import goormton.team.gotjob.domain.skill.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService svc;

    @GetMapping
    public ApiResponse<List<SkillResponse>> list(){
        return ApiResponse.ok(svc.list());
    }

    @PostMapping
    public ApiResponse<SkillResponse> create(@RequestBody SkillCreateRequest req){
        return ApiResponse.ok(svc.create(req));
    }

    @PostMapping("/link")
    public ApiResponse<SkillLinkResponse> link(@RequestBody SkillLinkRequest req){
        return ApiResponse.ok(svc.link(req));
    }

    @DeleteMapping("/link/{id}")
    public ApiResponse<String> unlink(@PathVariable Long id){
        svc.unlink(id); return ApiResponse.ok("Unlinked skill link " + id);
    }

}