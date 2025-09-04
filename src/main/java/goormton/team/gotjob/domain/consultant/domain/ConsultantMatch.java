package goormton.team.gotjob.domain.consultant.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="consultant_matches",
        uniqueConstraints=@UniqueConstraint(columnNames={"consultant_id","user_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConsultantMatch extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="consultant_id")
    private User consultant;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id")
    private User user;

    @Builder.Default
    @Column(name = "status")
    private String matchStatus = "ACTIVE";

    @Lob private String note;
}