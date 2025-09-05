package goormton.team.gotjob.domain.consultant.dto;

public record ConsultantMatchResponse(Long id, Long consultantId, Long userId,
                                      String status, String note, String createdAt) {}