package goormton.team.gotjob.domain.user.domain;


import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.consultant.domain.Specialty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // 아이디
    private String username;

    @Column(unique = true) // 이메일
    private String email;

    @Column(nullable = false) // 비밀번호
    private String password;

    @Column(nullable=false) // 이름
    private String realName;

    private String phone; // 휴대폰 번호

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Specialty specialty;

    // 마이페이지 관련 필드들
    private String interestJob;      // 관심 직무
    private String skills;           // 보유 역량 (콤마 구분 문자열 or JSON 저장 가능)
    @Lob
    private String bio;              // 간단한 자기소개
    private String resumeUrl;        // 이력서 첨부 (S3 URL 등)
}
