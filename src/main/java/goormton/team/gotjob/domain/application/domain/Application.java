package goormton.team.gotjob.domain.application.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.job.domain.Job;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Table(name = "applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","job_id"}))
public class Application extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // PERSONAL user

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Builder.Default
    @Column(name="application_status")
    private String applicationStatus = "APPLIED"; // APPLIED, REVIEWING, INTERVIEW, OFFER, REJECTED, WITHDRAWN

    @Lob
    private String coverLetter;

    private String resumeUrlSnapshot;
}
