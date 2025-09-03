package goormton.team.gotjob.domain.user.dto;

public record UpdateMeRequest(String bio, Integer yearsExperience, String location,
                              String resumeUrl, String portfolioUrl) {}