package goormton.team.gotjob.domain.file.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.documentAi.dto.response.DocumentAiKeywordsResponse;
import goormton.team.gotjob.domain.keword.domain.Keyword;
import goormton.team.gotjob.domain.keword.domain.KeywordType;
import goormton.team.gotjob.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keyword> keywords = new ArrayList<>();


    @Builder
    public File(User user, String originalFileName, String storedFileName, String fileUrl) {
        this.user = user;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileUrl = fileUrl;
    }

    // ⭐️ 연관관계 편의 메서드 추가: 키워드 리스트를 받아서 File에 추가
    public void addKeywords(DocumentAiKeywordsResponse response) {
        // 희망 직무 키워드 추가
        response.preferredJob().forEach(keywordDto -> this.keywords.add(Keyword.builder()
                .file(this) // 연관관계의 주인인 File 객체를 설정
                .term(keywordDto.term())
                .weight(keywordDto.weight())
                .type(KeywordType.PREFERRED_JOB)
                .build()));

        // 기술 및 스펙 키워드 추가
        response.skillsAndSpecs().forEach(keywordDto -> this.keywords.add(Keyword.builder()
                .file(this) // 연관관계의 주인인 File 객체를 설정
                .term(keywordDto.term())
                .weight(keywordDto.weight())
                .type(KeywordType.SKILL_AND_SPEC)
                .build()));
    }
}