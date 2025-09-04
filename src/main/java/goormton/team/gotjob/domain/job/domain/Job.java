package goormton.team.gotjob.domain.job.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.company.domain.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity @Table(name="jobs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Job extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="company_id")
    private Company company;

    @Column(nullable=false) private String title;
    private String employmentType;
    private String location;
    private Integer minSalary;
    private Integer maxSalary;
    @Lob private String requirements;
    @Lob private String description;

    @Builder.Default
    @Column(name = "status")
    private String jobStatus = "OPEN";
    private LocalDate deadline;
}