package goormton.team.gotjob.domain.application.service;
import goormton.team.gotjob.domain.application.dto.*;
import goormton.team.gotjob.domain.application.domain.Application;
import goormton.team.gotjob.domain.application.repository.ApplicationRepository;
import goormton.team.gotjob.domain.job.domain.Job;
import goormton.team.gotjob.domain.job.repository.JobRepository;
import goormton.team.gotjob.domain.user.domain.User;
import goormton.team.gotjob.domain.user.repository.UserRepository;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service @RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applications;
    private final UserRepository users;
    private final JobRepository jobs;

    @Transactional
    public ApplicationResponse apply(Long userId, Long jobId, ApplyRequest req){
        User u = users.findById(userId).orElseThrow(()->new ApiException(404,"user not found"));
        Job j = jobs.findById(jobId).orElseThrow(()->new ApiException(404,"job not found"));
        applications.findByUserAndJob(u,j).ifPresent(a->{ throw new ApiException(409,"already applied");});
        var a = Application.builder().user(u).job(j).coverLetter(req.coverLetter()).resumeUrlSnapshot(req.resumeUrlSnapshot()).build();
        applications.save(a);
        return toDto(a);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> myApplications(Long userId){
        var u = users.findById(userId).orElseThrow(()->new ApiException(404,"user not found"));
        return applications.findByUser(u).stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> applicants(Long jobId){
        var j = jobs.findById(jobId).orElseThrow(()->new ApiException(404,"job not found"));
        return applications.findByJob(j).stream().map(this::toDto).toList();
    }

    @Transactional
    public ApplicationResponse updateStatus(Long id, ApplicationStatusUpdateRequest req){
        var a = applications.findById(id).orElseThrow(()->new ApiException(404,"application not found"));
        a.setStatus(req.status());
        return toDto(a);
    }

    private ApplicationResponse toDto(Application a){
        var fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return new ApplicationResponse(a.getId(), a.getUser().getId(), a.getUser().getRealName(),
                a.getJob().getId(), a.getJob().getTitle(), a.getJob().getCompany().getName(),
                a.getStatus(), a.getCreatedAt()!=null?a.getCreatedAt().format(fmt):null);
    }
}