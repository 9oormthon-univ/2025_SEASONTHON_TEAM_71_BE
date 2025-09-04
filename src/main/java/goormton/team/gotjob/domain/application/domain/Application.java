package goormton.team.gotjob.domain.application.domain;
import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.user.domain.User;
import goormton.team.gotjob.domain.job.domain.Job;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="applications", uniqueConstraints=@UniqueConstraint(columnNames={"user_id","job_id"}))
@AttributeOverride(name = "status", column = @Column(name = "row_status"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Application extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="job_id")
    private Job job;

    @Builder.Default
    @Column(name = "status")
    private String applicationStatus = "APPLIED"; // APPLIED, REVIEWING, INTERVIEW, OFFER, REJECTED, WITHDRAWN

    @Lob private String coverLetter;
    private String resumeUrlSnapshot;
}