package goormton.team.gotjob.domain.job.dto;

import java.time.LocalDate;

public record JobCreateRequest(
        String title,
        String employmentType,
        String location,
        Integer minSalary,
        Integer maxSalary,
        String requirements,
        String description,
        LocalDate deadline
) {}