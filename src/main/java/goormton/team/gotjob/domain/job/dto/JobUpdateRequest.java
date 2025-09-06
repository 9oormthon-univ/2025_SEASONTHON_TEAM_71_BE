package goormton.team.gotjob.domain.job.dto;

import java.time.LocalDate;

public record JobUpdateRequest(
        String title,
        String employmentType,
        String location,
        Integer minSalary,
        Integer maxSalary,
        String requirements,
        String description,
        String status,
        LocalDate deadline
) {}

