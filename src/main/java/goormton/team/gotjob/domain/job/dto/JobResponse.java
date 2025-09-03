package goormton.team.gotjob.domain.job.dto;

public record JobResponse(Long id, String title, String employmentType, String location,
                          Integer minSalary, Integer maxSalary, String requirements,
                          String description, String status, String deadline,
                          CompanySummary company) {}
