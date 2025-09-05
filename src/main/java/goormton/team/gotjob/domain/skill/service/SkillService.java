package goormton.team.gotjob.domain.skill.service;

import goormton.team.gotjob.domain.skill.dto.*;
import goormton.team.gotjob.domain.skill.domain.Skill;
import goormton.team.gotjob.domain.skill.domain.SkillLink;
import goormton.team.gotjob.domain.skill.repository.SkillLinkRepository;
import goormton.team.gotjob.domain.skill.repository.SkillRepository;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skills;
    private final SkillLinkRepository links;

    @Transactional(readOnly = true)
    public List<SkillResponse> list(){
        return skills.findAll().stream().map(s->new SkillResponse(s.getId(), s.getName())).toList();
    }

    @Transactional
    public SkillResponse create(SkillCreateRequest req){
        if(req.name()==null || req.name().isBlank()) throw new ApiException(400,"skill name required");
        if(skills.existsByName(req.name())) throw new ApiException(409,"skill exists");
        Skill s = skills.save(Skill.builder().name(req.name().trim()).build());
        return new SkillResponse(s.getId(), s.getName());
    }

    @Transactional
    public SkillLinkResponse link(SkillLinkRequest req){
        var s = skills.findById(req.skillId()).orElseThrow(()->new ApiException(404,"skill not found"));
        SkillLink l = links.save(SkillLink.builder()
                .skill(s).targetType(req.targetType()).targetId(req.targetId())
                .level(req.level()).required(req.required()).weight(req.weight()).build());
        return new SkillLinkResponse(l.getId(), s.getId(), s.getName(), l.getTargetType(), l.getTargetId(), l.getLevel(), l.getRequired(), l.getWeight());
    }

    @Transactional
    public void unlink(Long linkId){
        if(!links.existsById(linkId)) throw new ApiException(404,"link not found");
        links.deleteById(linkId);
    }
}
