package goormton.team.gotjob.domain.job.dto;

import goormton.team.gotjob.domain.job.domain.Job;

import java.time.LocalDate;

public record JobResponse(
        Long id,
        Long companyId,
        String title,
        String employmentType,
        String location,
        Integer minSalary,
        Integer maxSalary,
        String requirements,
        String description,
        String status,
        LocalDate deadline
) {
    public static JobResponse of(Job j) {
        return new JobResponse(
                j.getId(),
                j.getCompany() != null ? j.getCompany().getId() : null,
                j.getTitle(),
                j.getEmploymentType(),
                j.getLocation(),
                j.getMinSalary(),
                j.getMaxSalary(),
                j.getRequirements(),
                j.getDescription(),
                j.getJobStatus(),
                j.getDeadline()
        );
    }
}