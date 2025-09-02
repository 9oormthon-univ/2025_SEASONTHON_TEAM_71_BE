package goormton.team.gotjob.domain.documentAi.application;

import com.google.cloud.documentai.v1.Document;
import com.google.cloud.documentai.v1.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1.ProcessRequest;
import com.google.cloud.documentai.v1.ProcessResponse;
import com.google.protobuf.ByteString;
import goormton.team.gotjob.domain.documentAi.dto.response.DocumentAiKeywordsResponse;
import goormton.team.gotjob.domain.documentAi.dto.response.DocumentAiSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentAiService {

    private final DocumentProcessorServiceClient docAiClient;

    public Map<DocumentAiSummaryResponse, List<DocumentAiKeywordsResponse>> getDocumentSummary(byte[] fileData, String projectId, String location, String summarizerId, String extractorId) {
        // 두 작업을 비동기로 동시에 실행하여 속도 향상
        CompletableFuture<String> summaryFuture = CompletableFuture.supplyAsync(() ->
                summarizeWithBuiltInProcessor(fileData, projectId, location, summarizerId)
        );

        CompletableFuture<List<DocumentAiKeywordsResponse>> extractionFuture = CompletableFuture.supplyAsync(() ->
                extractKeywardsWithWeights(fileData, projectId, location, extractorId)
        );

        // 두 작업이 모두 끝날 때까지 기다린 후 결과 조합
        DocumentAiSummaryResponse summary = new DocumentAiSummaryResponse(summaryFuture.join());
        List<DocumentAiKeywordsResponse> extractedData = extractionFuture.join();

        Map<DocumentAiSummaryResponse, List<DocumentAiKeywordsResponse>> result = new HashMap<>();
        result = (Map<DocumentAiSummaryResponse, List<DocumentAiKeywordsResponse>>) result.put(summary, extractedData);

        return result;
    }

    /**
     * Document AI의 내장 Summarizer 프로세서를 사용하여 PDF를 요약합니다.
     * @param fileData PDF 파일 바이트 배열
     * @param projectId GCP 프로젝트 ID
     * @param location 프로세서 리전 (예: "us")
     * @param summarizerProcessorId 생성한 Document Summarizer 프로세서 ID
     * @return 요약된 텍스트
     */
    public String summarizeWithBuiltInProcessor(byte[] fileData, String projectId, String location, String summarizerProcessorId) {
        String processorName = String.format("projects/%s/locations/%s/processors/%s", projectId, location, summarizerProcessorId);

        ProcessRequest request = ProcessRequest.newBuilder()
                .setName(processorName)
                .setInlineDocument(Document.newBuilder()
                        .setContent(ByteString.copyFrom(fileData))
                        .setMimeType("application/pdf"))
                .build();

        // Document AI API 호출
        ProcessResponse result = docAiClient.processDocument(request);
        Document resultDocument = result.getDocument();

        // 'summary_content' 타입을 가진 엔티티(Entity)에서 요약 내용을 추출합니다.
        List<String> summaries = resultDocument.getEntitiesList().stream()
                .filter(entity -> "summary_content".equals(entity.getType()))
                .map(Document.Entity::getMentionText)
                .collect(Collectors.toList());

        if (summaries.isEmpty()) {
            return "요약 내용을 찾을 수 없습니다.";
        }

        // 여러 개의 요약이 있을 경우 모두 합쳐서 반환
        return String.join("\n", summaries);
    }

    public List<DocumentAiKeywordsResponse> extractKeywardsWithWeights(byte[] fileData, String projectId, String location, String customProcessorId) {
        String processorName = String.format("projects/%s/locations/%s/processors/%s", projectId, location, customProcessorId);

        ProcessRequest request = ProcessRequest.newBuilder()
                .setName(processorName)
                .setInlineDocument(Document.newBuilder()
                        .setContent(ByteString.copyFrom(fileData))
                        .setMimeType("application/pdf"))
                .build();

        ProcessResponse result = docAiClient.processDocument(request);
        Document resultDocument = result.getDocument();

        List<DocumentAiKeywordsResponse> keywords = new ArrayList<>();

        // 추출된 엔티티(정보)들을 순회
        for (Document.Entity entity : resultDocument.getEntitiesList()) {
            // 1. 키워드(term) 추출
            String term = entity.getMentionText();

            // 2. 신뢰도 점수(confidence) 추출 (0.0 ~ 1.0 사이의 float)
            float confidence = entity.getConfidence();

            // 3. 신뢰도 점수를 0~100 사이의 정수 가중치(weight)로 변환
            int weight = (int) (confidence * 100);

            // 4. DTO 객체를 만들어 리스트에 추가
            keywords.add(new DocumentAiKeywordsResponse(term, weight));
        }

        return keywords;
    }
}
