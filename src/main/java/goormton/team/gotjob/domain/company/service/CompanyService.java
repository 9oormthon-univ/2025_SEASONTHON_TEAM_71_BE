package goormton.team.gotjob.domain.company.service;


import goormton.team.gotjob.domain.company.domain.Company;
import goormton.team.gotjob.domain.company.dto.CompanyCreateRequest;
import goormton.team.gotjob.domain.company.dto.CompanyResponse;
import goormton.team.gotjob.domain.company.dto.CompanyUpdateRequest;
import goormton.team.gotjob.domain.company.repository.CompanyRepository;
import goormton.team.gotjob.domain.user.domain.Role;
import goormton.team.gotjob.domain.user.domain.User;
import goormton.team.gotjob.domain.user.repository.UserRepository;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companies;
    private final UserRepository users;

    @Transactional
    public CompanyResponse create(Long ownerUserId, CompanyCreateRequest req) {
        User owner = users.findById(ownerUserId)
                .orElseThrow(() -> new ApiException(404, "owner not found"));
        if (owner.getRole() != Role.COMPANY) {
            throw new ApiException(403, "only COMPANY can create company profile");
        }
        companies.findByOwnerUserId(ownerUserId).ifPresent(c -> {
            throw new ApiException(409, "company already exists for this owner");
        });

        if (req.businessNo() != null && companies.existsByBusinessNo(req.businessNo())) {
            throw new ApiException(409, "businessNo already exists");
        }

        Company c = Company.builder()
                .name(req.name())
                .businessNo(req.businessNo())
                .description(req.description())
                .size(req.size())
                .website(req.website())
                .address(req.address())
                .ownerUserId(ownerUserId)
                .build();

        companies.save(c);
        return CompanyResponse.of(c);
    }

    @Transactional(readOnly = true)
    public CompanyResponse get(Long id) {
        Company c = companies.findById(id)
                .orElseThrow(() -> new ApiException(404, "company not found"));
        return CompanyResponse.of(c);
    }

    @Transactional
    public CompanyResponse update(Long id, CompanyUpdateRequest req, Long callerUserId) {
        Company c = companies.findById(id)
                .orElseThrow(() -> new ApiException(404, "company not found"));

        // 소유자만 수정 가능
        if (!c.getOwnerUserId().equals(callerUserId)) {
            throw new ApiException(403, "only owner can update the company");
        }

        if (req.name() != null) c.setName(req.name());
        if (req.businessNo() != null) {
            if (!req.businessNo().equals(c.getBusinessNo())
                    && companies.existsByBusinessNo(req.businessNo())) {
                throw new ApiException(409, "businessNo already exists");
            }
            c.setBusinessNo(req.businessNo());
        }
        if (req.description() != null) c.setDescription(req.description());
        if (req.size() != null) c.setSize(req.size());
        if (req.website() != null) c.setWebsite(req.website());
        if (req.address() != null) c.setAddress(req.address());

        return CompanyResponse.of(c);
    }
}
