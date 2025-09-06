package goormton.team.gotjob.domain.company.repository;

import goormton.team.gotjob.domain.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByOwnerUserId(Long ownerUserId);
    boolean existsByBusinessNo(String businessNo);
}