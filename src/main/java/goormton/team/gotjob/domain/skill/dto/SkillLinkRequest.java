package goormton.team.gotjob.domain.skill.dto;

public record SkillLinkRequest(Long skillId, String targetType, Long targetId,
                               Integer level, Boolean required, Integer weight) {}
