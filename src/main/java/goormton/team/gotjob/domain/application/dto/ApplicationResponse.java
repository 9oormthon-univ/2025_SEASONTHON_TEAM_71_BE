package goormton.team.gotjob.domain.application.dto;

import goormton.team.gotjob.domain.application.domain.Application;
import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long jobId,
        Long userId,
        String status,
        String coverLetter,
        String resumeUrlSnapshot,
        LocalDateTime createdAt
) {
    public static ApplicationResponse of(Application a) {
        return new ApplicationResponse(
                a.getId(),
                a.getJob().getId(),
                a.getUserId(),
                a.getApplicationStatus(),
                a.getCoverLetter(),
                a.getResumeUrlSnapshot(),
                a.getCreatedAt()
        );
    }
}