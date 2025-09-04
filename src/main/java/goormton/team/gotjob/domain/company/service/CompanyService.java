package goormton.team.gotjob.domain.company.service;

import goormton.team.gotjob.domain.company.dto.*;
import goormton.team.gotjob.domain.company.domain.Company;
import goormton.team.gotjob.domain.company.repository.CompanyRepository;
import goormton.team.gotjob.domain.user.repository.UserRepository;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companies;
    private final UserRepository users;

    @Transactional
    public CompanyResponse create(Long ownerUserId, CompanyCreateRequest req){
        var owner = users.findById(ownerUserId).orElseThrow(()->new ApiException(404,"owner not found"));
        var c = Company.builder()
                .name(req.name()).businessNo(req.businessNo()).description(req.description())
                .size(req.size()).website(req.website()).address(req.address())
                .owner(owner).build();
        companies.save(c);
        return toDto(c);
    }

    @Transactional(readOnly = true)
    public CompanyResponse get(Long id){ return toDto(companies.findById(id).orElseThrow(()->new ApiException(404,"company not found"))); }

    @Transactional
    public CompanyResponse update(Long id, CompanyUpdateRequest req){
        var c = companies.findById(id).orElseThrow(()->new ApiException(404,"company not found"));
        c.setName(req.name()); c.setBusinessNo(req.businessNo()); c.setDescription(req.description());
        c.setSize(req.size()); c.setWebsite(req.website()); c.setAddress(req.address());
        return toDto(c);
    }

    private CompanyResponse toDto(Company c){
        return new CompanyResponse(c.getId(), c.getName(), c.getBusinessNo(), c.getDescription(),
                c.getSize(), c.getWebsite(), c.getAddress(), c.getOwner()!=null?c.getOwner().getId():null);
    }
}
