package goormton.team.gotjob.domain.user.dto;

public record UpdateMeRequest(
        String interestJob,
        String skills,
        String bio,
        String resumeUrl
) {}