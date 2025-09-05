package goormton.team.gotjob.domain.company.repository;

import goormton.team.gotjob.domain.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByBusinessNo(String businessNo);
}
