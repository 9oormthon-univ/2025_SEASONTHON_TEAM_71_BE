package goormton.team.gotjob.domain.documentAi.presentation;

import goormton.team.gotjob.domain.documentAi.application.DocumentAiService;
import goormton.team.gotjob.domain.documentAi.dto.response.DocumentAiKeywordsResponse;
import goormton.team.gotjob.domain.documentAi.dto.response.DocumentAiSummaryResponse;
import goormton.team.gotjob.global.payload.ResponseCustom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "DocumentAI 서비스 호출 API", description = "유저가 pdf 파일 형태로 올린 이력서를 입력받아 요약 및 키워드 추출 등의 서비스를 실행하는 controller입니다.")
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class DocumentAiController {

    private final DocumentAiService documentAiService;

    @Operation(summary = "이력서 요약", description = "pdf 형식의 이력서 파일을 입력받아 요약된 내용을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/summary/{fileId}")
    public ResponseCustom<?> summaryResume(@PathVariable Long fileId) throws IOException {
        DocumentAiSummaryResponse response = documentAiService.summarizeWithBuiltInProcessor(fileId);
        return ResponseCustom.OK(response);
    }

    @Operation(summary = "키워드 추출", description = "pdf 형식의 이력서 파일을 입력받아 이력서 내 중요하게 여겨지는 키워드를 추출합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "실패")
    })
    @GetMapping("/extract/{fileId}")
    public ResponseCustom<?> ExtractKeywords(@PathVariable Long fileId) throws IOException {
        DocumentAiKeywordsResponse response = documentAiService.extractKeywordsWithWeights(fileId);
        return ResponseCustom.OK(response);
    }
}
