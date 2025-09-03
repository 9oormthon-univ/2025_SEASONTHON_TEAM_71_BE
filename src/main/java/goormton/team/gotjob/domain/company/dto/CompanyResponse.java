package goormton.team.gotjob.domain.company.dto;

public record CompanyResponse(Long id, String name, String businessNo, String description,
                              String size, String website, String address, Long ownerUserId) {}
