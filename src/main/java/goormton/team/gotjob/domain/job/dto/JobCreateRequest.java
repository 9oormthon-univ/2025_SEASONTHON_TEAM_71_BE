package goormton.team.gotjob.domain.job.dto;

public record JobCreateRequest(Long companyId, String title, String employmentType,
                               String location, Integer minSalary, Integer maxSalary,
                               String requirements, String description, String deadline) {}