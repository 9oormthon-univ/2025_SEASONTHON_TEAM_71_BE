package goormton.team.gotjob.domain.company.dto;

public record CompanyUpdateRequest(String name, String businessNo, String description,
                                   String size, String website, String address) {}