package goormton.team.gotjob.domain.keword.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.file.domain.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private File file; // 연관관계의 주인

    @Column(nullable = false)
    private String term; // 키워드 내용

    @Column(nullable = false)
    private int weight; // 가중치

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KeywordType type; // 키워드 종류

    @Builder(toBuilder = true)
    public Keyword(File file, String term, int weight, KeywordType type) {
        this.file = file;
        this.term = term;
        this.weight = weight;
        this.type = type;
    }
}
