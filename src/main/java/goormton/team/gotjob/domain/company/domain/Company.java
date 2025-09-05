package goormton.team.gotjob.domain.company.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="companies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Company extends BaseEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(unique=true)
    private String businessNo;

    @Lob private String description;
    private String size;   // S,M,L
    private String website;
    private String address;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="owner_user_id")
    private User owner;
}