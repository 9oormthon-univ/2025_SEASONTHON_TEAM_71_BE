package goormton.team.gotjob.domain.application.dto;

public record ApplicationResponse(Long id, Long userId, String userName,
                                  Long jobId, String jobTitle, String companyName,
                                  String status, String appliedAt) {}