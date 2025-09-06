package goormton.team.gotjob.domain.company.dto;

public record CompanyCreateRequest(
        String name,
        String businessNo,
        String description,
        String size,
        String website,
        String address
) {}