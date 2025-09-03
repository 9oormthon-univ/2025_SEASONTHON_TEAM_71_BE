package goormton.team.gotjob.domain.user.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {}