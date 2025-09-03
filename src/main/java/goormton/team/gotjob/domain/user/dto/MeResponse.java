package goormton.team.gotjob.domain.user.dto;

public record MeResponse(Long userId, String username, String email, String role,
                         String bio, Integer yearsExperience, String location,
                         String resumeUrl, String portfolioUrl) {}
