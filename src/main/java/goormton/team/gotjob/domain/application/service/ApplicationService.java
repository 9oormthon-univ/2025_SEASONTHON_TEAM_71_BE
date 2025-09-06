package goormton.team.gotjob.domain.application.service;

import goormton.team.gotjob.domain.application.domain.Application;
import goormton.team.gotjob.domain.application.dto.ApplyRequest;
import goormton.team.gotjob.domain.application.dto.ApplicationResponse;
import goormton.team.gotjob.domain.application.repository.ApplicationRepository;
import goormton.team.gotjob.domain.job.domain.Job;
import goormton.team.gotjob.domain.job.repository.JobRepository;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applications;
    private final JobRepository jobs;

    @Transactional
    public ApplicationResponse apply(Long userId, Long jobId, ApplyRequest req) {
        Job j = jobs.findById(jobId).orElseThrow(() -> new ApiException(404, "job not found"));
        if (!"OPEN".equalsIgnoreCase(j.getJobStatus())) {
            throw new ApiException(400, "job is not open");
        }
        if (applications.existsByUserIdAndJobId(userId, jobId)) {
            throw new ApiException(409, "already applied");
        }

        Application a = Application.builder()
                .userId(userId)
                .job(j)
                .applicationStatus("APPLIED")
                .coverLetter(req.coverLetter())
                .resumeUrlSnapshot(req.resumeUrlSnapshot())
                .build();
        applications.save(a);

        return ApplicationResponse.of(a);
    }
}
