package goormton.team.gotjob.domain.user.domain;


import goormton.team.gotjob.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Table(name = "users")
@AttributeOverride(name = "status", column = @Column(name = "row_status"))
@NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    @Column(nullable=false)
    private String realName;

    private String phone;

    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private Role role;

    @Builder.Default
    @Enumerated(EnumType.STRING) @Column(nullable=false)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @OneToOne(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
    private UserProfile profile;

    public void attachProfile(UserProfile p){ this.profile=p; p.setUser(this); }

}
