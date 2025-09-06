package goormton.team.gotjob.domain.company.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Table(name = "companies")
public class Company extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String businessNo;

    private String description;
    private String size;     // S, M, L
    private String website;
    private String address;

    @Column(nullable = false)
    private Long ownerUserId; // users.id 참조
}
