package goormton.team.gotjob.domain.skill.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="skill_links",
        uniqueConstraints=@UniqueConstraint(columnNames={"target_type","target_id","skill_id"}),
        indexes={ @Index(name="idx_skill_id",columnList="skill_id"),
                @Index(name="idx_target", columnList="target_type,target_id")})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SkillLink extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="skill_id", nullable=false)
    private Skill skill;

    @Column(name="target_type", nullable=false)
    private String targetType; // USER or JOB

    @Column(name="target_id", nullable=false)
    private Long targetId;

    private Integer level;     // USER
    private Boolean required;  // JOB
    private Integer weight;    // JOB
}