package goormton.team.gotjob.domain.job.dto;

public record JobUpdateRequest(String title, String employmentType,
                               String location, Integer minSalary, Integer maxSalary,
                               String requirements, String description, String status,
                               String deadline) {}
