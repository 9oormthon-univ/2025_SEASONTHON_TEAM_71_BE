package goormton.team.gotjob.domain.skill.dto;

public record SkillLinkResponse(Long id, Long skillId, String skillName,
                                String targetType, Long targetId, Integer level,
                                Boolean required, Integer weight) {}