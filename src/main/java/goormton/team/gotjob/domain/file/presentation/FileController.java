package goormton.team.gotjob.domain.file.presentation;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import goormton.team.gotjob.domain.file.application.FileService;
import goormton.team.gotjob.domain.file.dto.response.FileDownloadResponse;
import goormton.team.gotjob.global.payload.ResponseCustom;
import goormton.team.gotjob.global.security.JwtProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Tag(name = "파일 관리 API", description = "유저가 이력서를 pdf 파일 형태로 업로드 및 다운로드하는 api입니다.")
@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "pdf 파일 형식의 이력서 업로드", description = "pdf 형식의 이력서 파일을 입력받아 AWS s3 버킷에 업로드합니다.")
    @PostMapping()
    public ResponseCustom<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token) {
        log.info("JWT: {}", token);
        String fileUrl = fileService.uploadAndSaveFile(file, token);
        return ResponseCustom.OK();
    }

    @Operation(summary = "pdf 파일 형식의 이력서 다운로드", description = "fileId를 통해 기존에 업로드해두었던 이력서 pdf 파일을 다운로드합니다.")
    @Parameter(name = "fileId", description = "다운로드할 파일의 고유 ID", required = true)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "파일 다운로드 성공",
                    // 응답 본문이 어떤 형태인지 명시합니다. (바이너리 파일)
                    content = @Content(mediaType = "application/octet-stream",
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 파일을 찾을 수 없음",
                    // 응답 본문이 없을 경우 @Content를 생략하거나, 에러 DTO 스키마를 명시할 수 있습니다.
                    content = @Content)
    })
    @GetMapping("/{fileId}")
    public ResponseEntity downloadFile(@PathVariable Long fileId, @RequestHeader("Authorization") String token) throws UnsupportedEncodingException {
        try {
            // 1. FileService를 통해 파일 다운로드 정보 가져오기
            FileDownloadResponse downloadDto = fileService.downloadFile(fileId, token);

            S3Object s3Object = downloadDto.s3Object();
            String originalFileName = downloadDto.originalFilename();

            // 2. S3Object에서 InputStream 추출
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            Resource resource = new InputStreamResource(inputStream);

            // 3. 다운로드될 파일 이름 인코딩 처리 (한글 등 깨짐 방지)
            String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

            // 4. HTTP 응답 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // 일반적인 바이너리 파일 타입
            headers.setContentDispositionFormData("attachment", encodedFileName); // attachment로 설정하여 다운로드 유도

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            // 파일 조회 실패 또는 다른 예외 처리
            // 예: 로그를 남기고, 적절한 에러 응답을 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
