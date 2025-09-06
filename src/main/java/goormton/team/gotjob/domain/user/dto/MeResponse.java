package goormton.team.gotjob.domain.user.dto;

public record MeResponse(
        Long userId,
        String username,
        String email,
        String role,
        String realName,
        String phone,
        String interestJob,
        String skills,
        String bio,
        String resumeUrl
) {}