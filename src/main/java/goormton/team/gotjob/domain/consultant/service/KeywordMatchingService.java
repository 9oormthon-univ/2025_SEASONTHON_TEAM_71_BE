package goormton.team.gotjob.domain.consultant.service;

import goormton.team.gotjob.domain.consultant.domain.Specialty;
import goormton.team.gotjob.domain.consultant.domain.repository.ConsultantMatchRepository;
import goormton.team.gotjob.domain.consultant.domain.repository.SpecialtyRepository;
import goormton.team.gotjob.domain.consultant.dto.KeywordMatchDto;
import goormton.team.gotjob.domain.keword.domain.Keyword;
import goormton.team.gotjob.domain.keword.domain.repository.KeywordRepository;
import goormton.team.gotjob.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordMatchingService {

    private final KeywordRepository keywordRepository;
    private final SpecialtyRepository specialtyRepository;
    private final ConsultantMatchRepository consultantMatchRepository;
    private final CosineSimilarity cosineSimilarity = new CosineSimilarity();

    // 특정 파일을 기준으로 가장 적합한 컨설턴트를 찾아 유사도 순으로 반환.
    public List<KeywordMatchDto> findMatchingConsultants(Long fileId) {
        // 1. 기준 파일의 모든 키워드를 가져와 하나의 벡터로 만듭니다.
        List<Keyword> fileKeywords = keywordRepository.findByFileId(fileId);
        Map<CharSequence, Integer> fileVector = fileKeywords.stream()
                .collect(Collectors.toMap(Keyword::getTerm, keyword -> 1, Integer::sum));

        if (fileVector.isEmpty()) {
            return List.of(); // 기준 파일에 키워드가 없으면 빈 리스트 반환
        }

        // 2. 매칭이 이미 진행된 컨설턴트 ID 목록 조회
        Set<Long> matchedConsultantIds = consultantMatchRepository.findConsultantsByMatchStatus("ACTIVE")
                .stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        // 3. 모든 컨설턴트의 전문 분야 키워드를 가져와 컨설턴트(User ID)별로 그룹화합니다.
        Map<Long, List<Specialty>> consultantSpecialties = specialtyRepository.findAll().stream()
                .collect(Collectors.groupingBy(specialty -> specialty.getUser().getId()));

        List<KeywordMatchDto> results = new ArrayList<>();

        // 4. 각 컨설턴트의 전문 분야와 파일 키워드 간의 유사도를 계산.
        for (Map.Entry<Long, List<Specialty>> entry : consultantSpecialties.entrySet()) {
            Long consultantId = entry.getKey();

            // 5. 이미 매칭된 컨설턴트는 건너뛰기 (핵심 변경 사항)
            if (matchedConsultantIds.contains(consultantId)) {
                continue;
            }

            List<Specialty> specialties = entry.getValue();

            // 컨설턴트의 전문 분야 키워드 리스트를 벡터로 변환
            Map<CharSequence, Integer> consultantVector = specialties.stream()
                    .collect(Collectors.toMap(Specialty::getKeyword, specialty -> 1, Integer::sum));

            if (consultantVector.isEmpty()) {
                continue;
            }

            // 4. 코사인 유사도 계산
            double score = cosineSimilarity.cosineSimilarity(fileVector, consultantVector);

            if (score > 0) {
                results.add(KeywordMatchDto.of(consultantId, score));
            }
        }

        // 5. 유사도 점수가 높은 순으로 정렬
        results.sort((r1, r2) -> Double.compare(r2.score(), r1.score()));

        return results;
    }
}
