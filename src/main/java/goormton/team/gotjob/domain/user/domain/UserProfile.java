package goormton.team.gotjob.domain.user.domain;
import goormton.team.gotjob.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfile extends BaseEntity {
    @Id private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)@JoinColumn(name = "user_id")
    private User user;

    @Lob
    private String bio;
    private Integer yearsExperience;
    private String location;
    private String resumeUrl;
    private String portfolioUrl;

}