// package goormton.team.gotjob.domain.documentAi.application;

// import com.amazonaws.services.s3.model.S3Object;
// import com.amazonaws.services.s3.model.S3ObjectInputStream;
// import com.google.cloud.documentai.v1.Document;
// import com.google.cloud.documentai.v1.DocumentProcessorServiceClient;
// import com.google.cloud.documentai.v1.ProcessRequest;
// import com.google.cloud.documentai.v1.ProcessResponse;
// import com.google.protobuf.ByteString;
// import goormton.team.gotjob.domain.documentAi.dto.response.DocumentAiKeywordsResponse;
// import goormton.team.gotjob.domain.documentAi.dto.response.DocumentAiSummaryResponse;
// import goormton.team.gotjob.domain.file.domain.File;
// import goormton.team.gotjob.domain.file.domain.repository.FileRepository;
// import goormton.team.gotjob.domain.keword.domain.Keyword;
// import goormton.team.gotjob.domain.keword.domain.KeywordType;
// import goormton.team.gotjob.domain.keword.domain.repository.KeywordRepository;
// import goormton.team.gotjob.global.error.DefaultException;
// import goormton.team.gotjob.global.payload.ErrorCode;
// import goormton.team.gotjob.global.service.S3Service;
// import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.CompletableFuture;
// import java.util.stream.Collectors;

// @Service
// @Transactional
// @RequiredArgsConstructor
// public class DocumentAiService {

//     private final FileRepository fileRepository;
//     private final DocumentProcessorServiceClient docAiClient;
//     private final KeywordRepository keywordRepository;
//     private final S3Service s3Service;

//     @Value("${gcp.project-id}")
//     private String projectId;

//     @Value("${gcp.location}")
//     private String location;

//     @Value("${gcp.processor.summarizer-id}")
//     private String summarizerId;

//     @Value("${gcp.processor.extractor-id}")
//     private String extractorId;

//     public Map<DocumentAiSummaryResponse, DocumentAiKeywordsResponse> getDocumentSummaryAndExtract(Long fileId) {
//         // 두 작업을 비동기로 동시에 실행하여 속도 향상
//         CompletableFuture<DocumentAiSummaryResponse> summaryFuture = CompletableFuture.supplyAsync(() ->
//                 summarizeWithBuiltInProcessor(fileId)
//         );

//         CompletableFuture<DocumentAiKeywordsResponse> extractionFuture = CompletableFuture.supplyAsync(() ->
//                 extractKeywordsWithWeights(fileId)
//         );

//         // 두 작업이 모두 끝날 때까지 기다린 후 결과 조합
//         DocumentAiSummaryResponse summary = summaryFuture.join();
//         DocumentAiKeywordsResponse extractedData = extractionFuture.join();

//         Map<DocumentAiSummaryResponse, DocumentAiKeywordsResponse> result = new HashMap<>();
//         result.put(summary, extractedData);

//         return result;
//     }

//     //Document AI의 내장 Summarizer 프로세서를 사용하여 PDF를 요약합니다.
//     public DocumentAiSummaryResponse summarizeWithBuiltInProcessor(Long fileId) {
//         // 1. fileId로 File 엔티티를 DB에서 조회합니다.
//         //    파일이 없으면 예외를 발생시킵니다.
//         File file = fileRepository.findById(fileId)
//                 .orElseThrow(() -> new DefaultException(ErrorCode.FILE_NOT_FOUND));

//         S3Object s3Object = s3Service.downloadFile(file.getStoredFileName());
//         byte[] fileData = s32byte(s3Object);

//         String processorName = String.format("projects/%s/locations/%s/processors/%s", projectId, location, summarizerId);

//         ProcessRequest request = ProcessRequest.newBuilder()
//                 .setName(processorName)
//                 .setInlineDocument(Document.newBuilder()
//                         .setContent(ByteString.copyFrom(fileData))
//                         .setMimeType("application/pdf"))
//                 .build();

//         // Document AI API 호출
//         ProcessResponse result = docAiClient.processDocument(request);
//         Document resultDocument = result.getDocument();

//         // 'summary_content' 타입을 가진 엔티티(Entity)에서 요약 내용을 추출합니다.
//         List<String> summaries = resultDocument.getEntitiesList().stream()
//                 .filter(entity -> "summary_content".equals(entity.getType()))
//                 .map(Document.Entity::getMentionText)
//                 .collect(Collectors.toList());

//         if (summaries.isEmpty()) {
//             throw new DefaultException(ErrorCode.INVALID_DOCUMENTAI_SUMMARY);
//         }

//         // 여러 개의 요약이 있을 경우 모두 합쳐서 반환
//         String response = String.join("\n", summaries);
//         return new DocumentAiSummaryResponse(response);
//     }

//     public DocumentAiKeywordsResponse extractKeywordsWithWeights(Long fileId) {
//         // 1. fileId로 File 엔티티를 DB에서 조회합니다.
//         //    파일이 없으면 예외를 발생시킵니다.
//         File file = fileRepository.findById(fileId)
//                 .orElseThrow(() -> new DefaultException(ErrorCode.FILE_NOT_FOUND));

//         S3Object s3Object = s3Service.downloadFile(file.getStoredFileName());

//         byte[] fileData = s32byte(s3Object);

//         String processorName = String.format("projects/%s/locations/%s/processors/%s", projectId, location, extractorId);

//         ProcessRequest request = ProcessRequest.newBuilder()
//                 .setName(processorName)
//                 .setInlineDocument(Document.newBuilder()
//                         .setContent(ByteString.copyFrom(fileData))
//                         .setMimeType("application/pdf"))
//                 .build();

//         ProcessResponse result = docAiClient.processDocument(request);
//         Document resultDocument = result.getDocument();

//         // 임시로 모든 키워드를 담을 리스트
//         List<Keyword> rawPreferredJobs = new ArrayList<>();
//         List<Keyword> rawSkillsAndSpecs = new ArrayList<>();

//         // 추출된 엔티티(정보)들을 순회
//         for (Document.Entity entity : resultDocument.getEntitiesList()) {
//             String term = entity.getMentionText();
//             int weight = (int) (entity.getConfidence() * 100);

//             // 엔티티 타입에 따라 해당하는 임시 리스트에 Keyword 객체 추가
//             switch (entity.getType()) {
//                 case "preferred_job":
//                     rawPreferredJobs.add(Keyword.builder()
//                             .file(file)
//                             .type(KeywordType.PREFERRED_JOB)
//                             .term(term) // 원본 텍스트 저장
//                             .weight(weight)
//                             .build());
//                     break;
//                 case "skill_or_spec":
//                     rawSkillsAndSpecs.add(Keyword.builder()
//                             .file(file)
//                             .type(KeywordType.SKILL_AND_SPEC)
//                             .term(term) // 원본 텍스트 저장
//                             .weight(weight)
//                             .build());
//                     break;
//             }
//         }

//         // 정규화 및 중복 제거 로직 적용
//         List<Keyword> finalPreferredJobs = deduplicateAndNormalizeKeywords(rawPreferredJobs);
//         List<Keyword> finalSkillsAndSpecs = deduplicateAndNormalizeKeywords(rawSkillsAndSpecs);

//         List<Keyword> allKeywordsToSave = new ArrayList<>();
//         allKeywordsToSave.addAll(finalPreferredJobs);
//         allKeywordsToSave.addAll(finalSkillsAndSpecs);

// // 합쳐진 리스트를 saveAll 메소드에 단 한 번만 전달하여 저장합니다.
//         keywordRepository.saveAll(allKeywordsToSave);

//         return new DocumentAiKeywordsResponse(finalPreferredJobs, finalSkillsAndSpecs);
//     }

//     private List<Keyword> deduplicateAndNormalizeKeywords(List<Keyword> keywords) {
//         // Key: 정규화된 키워드 (대문자 + 공백제거), Value: Keyword 객체
//         Map<String, Keyword> uniqueKeywordsMap = new HashMap<>();

//         for (Keyword currentKeyword : keywords) {
//             // 1. 키워드 정규화: 모든 공백 제거 및 대문자 변환
//             String normalizedTerm = currentKeyword.getTerm().replaceAll("\\s+", "").toUpperCase();

//             // 2. 중복 검사
//             // 맵에 없거나, 있더라도 현재 키워드의 가중치(weight)가 더 높으면 맵에 추가/교체
//             if (!uniqueKeywordsMap.containsKey(normalizedTerm) ||
//                     uniqueKeywordsMap.get(normalizedTerm).getWeight() < currentKeyword.getWeight()) {

//                 // 3. 원본 키워드의 term을 대문자로 통일
//                 currentKeyword.toBuilder()
//                         .term(currentKeyword.getTerm().toUpperCase())
//                         .build();
//                 uniqueKeywordsMap.put(normalizedTerm, currentKeyword);
//             }
//         }

//         // 맵의 값들(유니크한 Keyword 객체들)을 리스트로 변환하여 반환
//         return new ArrayList<>(uniqueKeywordsMap.values());
//     }

//     private byte[] s32byte(S3Object s3Object) {
//         byte[] fileData;
//         try (S3ObjectInputStream inputStream = s3Object.getObjectContent();
//              ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//             byte[] buffer = new byte[1024];
//             int bytesRead;
//             while ((bytesRead = inputStream.read(buffer)) != -1) {
//                 outputStream.write(buffer, 0, bytesRead);
//             }
//             return outputStream.toByteArray();
//         } catch (IOException e) {
//             // 파일 데이터를 읽는 중 오류 발생 시 예외 처리
//             throw new RuntimeException("Failed to read file data from S3", e);
//         }
//     }
// }
