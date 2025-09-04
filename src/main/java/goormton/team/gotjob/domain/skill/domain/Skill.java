package goormton.team.gotjob.domain.skill.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="skills")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Skill {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;
}