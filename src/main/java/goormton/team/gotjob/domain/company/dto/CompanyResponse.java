package goormton.team.gotjob.domain.company.dto;

import goormton.team.gotjob.domain.company.domain.Company;

public record CompanyResponse(
        Long id,
        String name,
        String businessNo,
        String description,
        String size,
        String website,
        String address,
        Long ownerUserId
) {
    public static CompanyResponse of(Company c) {
        return new CompanyResponse(
                c.getId(),
                c.getName(),
                c.getBusinessNo(),
                c.getDescription(),
                c.getSize(),
                c.getWebsite(),
                c.getAddress(),
                c.getOwnerUserId()
        );
    }
}