package goormton.team.gotjob.domain.documentAi.application;

import com.google.cloud.documentai.v1.Document;
import com.google.cloud.documentai.v1.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1.ProcessRequest;
import com.google.cloud.documentai.v1.ProcessResponse;
import com.google.protobuf.ByteString;
import goormton.team.gotjob.domain.documentAi.dto.response.DocumentAiKeywordsResponse;
import goormton.team.gotjob.domain.documentAi.dto.response.DocumentAiSummaryResponse;
import goormton.team.gotjob.global.error.DefaultException;
import goormton.team.gotjob.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.location}")
    private String location;

    @Value("${gcp.processor.summarizer-id}")
    private String summarizerId;

    @Value("${gcp.processor.extractor-id}")
    private String extractorId;

    public Map<DocumentAiSummaryResponse, List<DocumentAiKeywordsResponse>> getDocumentSummaryAndExtract(byte[] fileData) {
        // 두 작업을 비동기로 동시에 실행하여 속도 향상
        CompletableFuture<DocumentAiSummaryResponse> summaryFuture = CompletableFuture.supplyAsync(() ->
                summarizeWithBuiltInProcessor(fileData)
        );

        CompletableFuture<List<DocumentAiKeywordsResponse>> extractionFuture = CompletableFuture.supplyAsync(() ->
                extractKeywardsWithWeights(fileData)
        );

        // 두 작업이 모두 끝날 때까지 기다린 후 결과 조합
        DocumentAiSummaryResponse summary = summaryFuture.join();
        List<DocumentAiKeywordsResponse> extractedData = extractionFuture.join();

        Map<DocumentAiSummaryResponse, List<DocumentAiKeywordsResponse>> result = new HashMap<>();
        result = (Map<DocumentAiSummaryResponse, List<DocumentAiKeywordsResponse>>) result.put(summary, extractedData);

        return result;
    }

    /**
     * Document AI의 내장 Summarizer 프로세서를 사용하여 PDF를 요약합니다.
     * @param fileData PDF 파일 바이트 배열
     * @return 요약된 텍스트
     */
    public DocumentAiSummaryResponse summarizeWithBuiltInProcessor(byte[] fileData) {
        String processorName = String.format("projects/%s/locations/%s/processors/%s", projectId, location, summarizerId);

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
            throw new DefaultException(ErrorCode.INVALID_DOCUMENTAI_SUMMARY);
        }

        // 여러 개의 요약이 있을 경우 모두 합쳐서 반환
        String response = String.join("\n", summaries);
        return new DocumentAiSummaryResponse(response);
    }

    public List<DocumentAiKeywordsResponse> extractKeywardsWithWeights(byte[] fileData) {
        String processorName = String.format("projects/%s/locations/%s/processors/%s", projectId, location, extractorId);

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

        if (keywords.isEmpty()) {
            throw new DefaultException(ErrorCode.INVALID_DOCUMENTAI_KEYWORDS_EXTRACT);
        }

        return keywords;
    }
}
