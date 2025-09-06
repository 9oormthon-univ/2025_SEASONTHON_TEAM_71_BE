package goormton.team.gotjob.domain.file.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseEntity {

    // 누가 올렸는지 식별하기 위한 유저 ID
    // 실제로는 User 엔티티와 @ManyToOne 관계를 맺는 것이 더 좋습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String originalFileName; // 사용자가 업로드한 파일 원본명

    @Column(nullable = false, unique = true)
    private String storedFileName; // S3에 저장될 때 사용된 파일명 (UUID 등으로 생성)

    @Column(nullable = false)
    private String fileUrl; // S3에 업로드된 파일의 URL

    @Builder
    public File(User user, String originalFileName, String storedFileName, String fileUrl) {
        this.user = user;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileUrl = fileUrl;
    }
}
