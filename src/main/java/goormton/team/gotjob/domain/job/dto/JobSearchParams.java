package goormton.team.gotjob.domain.job.dto;

public record JobSearchParams(
        String q,
        String location,
        String employmentType,
        String status,
        Integer page,
        Integer size
) {}