package goormton.team.gotjob.domain.job.service;

import goormton.team.gotjob.domain.company.repository.CompanyRepository;
import goormton.team.gotjob.domain.job.dto.*;
import goormton.team.gotjob.domain.job.domain.Job;
import goormton.team.gotjob.domain.job.repository.JobRepository;
import goormton.team.gotjob.domain.common.PagedResponse;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class JobService {
    private final JobRepository jobs;
    private final CompanyRepository companies;

    @Transactional
    public JobResponse create(JobCreateRequest req){
        var company = companies.findById(req.companyId()).orElseThrow(()->new ApiException(404,"company not found"));
        var j = Job.builder()
                .company(company).title(req.title()).employmentType(req.employmentType())
                .location(req.location()).minSalary(req.minSalary()).maxSalary(req.maxSalary())
                .requirements(req.requirements()).description(req.description())
                .deadline(req.deadline()!=null? java.time.LocalDate.parse(req.deadline()) : null)
                .jobStatus("OPEN").build();
        jobs.save(j);
        return toDto(j);
    }

    @Transactional(readOnly = true)
    public JobResponse get(Long id){ return toDto(jobs.findById(id).orElseThrow(()->new ApiException(404,"job not found"))); }

    @Transactional
    public JobResponse update(Long id, JobUpdateRequest req){
        var j = jobs.findById(id).orElseThrow(()->new ApiException(404,"job not found"));
        j.setTitle(req.title()); j.setEmploymentType(req.employmentType());
        j.setLocation(req.location()); j.setMinSalary(req.minSalary()); j.setMaxSalary(req.maxSalary());
        j.setRequirements(req.requirements()); j.setDescription(req.description());
        j.setDeadline(req.deadline()!=null? java.time.LocalDate.parse(req.deadline()) : null);
        j.setJobStatus(req.status());
        return toDto(j);
    }

    @Transactional(readOnly = true)
    public PagedResponse<JobResponse> search(JobSearchParams p){
        int page = p.page()==null?0:p.page(); int size = p.size()==null?10:p.size();
        var pg = jobs.findAll(PageRequest.of(page,size));
        var items = pg.map(this::toDto).toList();
        return new PagedResponse<>(items, page, size, pg.getTotalElements());
    }

    private JobResponse toDto(Job j){
        return new JobResponse(j.getId(), j.getTitle(), j.getEmploymentType(), j.getLocation(),
                j.getMinSalary(), j.getMaxSalary(), j.getRequirements(), j.getDescription(),
                j.getJobStatus(), j.getDeadline()!=null? j.getDeadline().toString():null,
                new CompanySummary(j.getCompany().getId(), j.getCompany().getName()));
    }
}