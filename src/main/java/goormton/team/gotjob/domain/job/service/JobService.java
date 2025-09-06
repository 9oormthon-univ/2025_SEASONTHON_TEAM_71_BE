package goormton.team.gotjob.domain.job.service;


import goormton.team.gotjob.domain.company.domain.Company;
import goormton.team.gotjob.domain.company.repository.CompanyRepository;
import goormton.team.gotjob.domain.job.domain.Job;
import goormton.team.gotjob.domain.job.dto.*;
import goormton.team.gotjob.domain.job.repository.JobRepository;
import goormton.team.gotjob.domain.job.repository.JobSpecifications;
import goormton.team.gotjob.domain.common.ApiException;
import goormton.team.gotjob.domain.common.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobs;
    private final CompanyRepository companies;

    @Transactional
    public JobResponse create(Long callerUserId, JobCreateRequest req) {
        // caller의 회사 가져오기(1인 1회사 기준)
        Company company = companies.findByOwnerUserId(callerUserId)
                .orElseThrow(() -> new ApiException(403, "owner has no company profile"));

        Job j = Job.builder()
                .company(company)
                .title(req.title())
                .employmentType(req.employmentType())
                .location(req.location())
                .minSalary(req.minSalary())
                .maxSalary(req.maxSalary())
                .requirements(req.requirements())
                .description(req.description())
                .jobStatus("OPEN")
                .deadline(req.deadline())
                .build();
        jobs.save(j);
        return JobResponse.of(j);
    }

    @Transactional(readOnly = true)
    public JobResponse get(Long id) {
        Job j = jobs.findById(id).orElseThrow(() -> new ApiException(404, "job not found"));
        return JobResponse.of(j);
    }

    @Transactional
    public JobResponse update(Long id, Long callerUserId, JobUpdateRequest req) {
        Job j = jobs.findById(id).orElseThrow(() -> new ApiException(404, "job not found"));

        // 소유 회사만 수정 가능
        Company ownerCompany = companies.findByOwnerUserId(callerUserId)
                .orElseThrow(() -> new ApiException(403, "owner has no company profile"));
        if (!j.getCompany().getId().equals(ownerCompany.getId())) {
            throw new ApiException(403, "only owner company can update this job");
        }

        if (req.title() != null) j.setTitle(req.title());
        if (req.employmentType() != null) j.setEmploymentType(req.employmentType());
        if (req.location() != null) j.setLocation(req.location());
        if (req.minSalary() != null) j.setMinSalary(req.minSalary());
        if (req.maxSalary() != null) j.setMaxSalary(req.maxSalary());
        if (req.requirements() != null) j.setRequirements(req.requirements());
        if (req.description() != null) j.setDescription(req.description());
        if (req.status() != null) j.setJobStatus(req.status());
        if (req.deadline() != null) j.setDeadline(req.deadline());

        return JobResponse.of(j);
    }

    @Transactional(readOnly = true)
    public PagedResponse<JobResponse> search(JobSearchParams params) {
        int page = params.page() != null ? Math.max(params.page(), 0) : 0;
        int size = params.size() != null ? Math.min(Math.max(params.size(), 1), 50) : 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Job> result = jobs.findAll(JobSpecifications.search(params), pageable);
        return PagedResponse.of(
                result.map(JobResponse::of).getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}
