package goormton.team.gotjob.domain.file.presentation;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import goormton.team.gotjob.domain.file.application.FileService;
import goormton.team.gotjob.domain.file.dto.response.FileDownloadResponse;
import goormton.team.gotjob.global.payload.ResponseCustom;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Tag(name = "파일 관리 API", description = "유저가 이력서를 pdf 파일 형태로 업로드 및 다운로드하는 api입니다.")
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseCustom<?> uploadFile(@RequestParam("file") MultipartFile file) {

//        Long currentUserId =

//        String fileUrl = fileService.uploadAndSaveFile(file, );

        return ResponseCustom.OK();
    }

    @GetMapping("/download/{fileId}")
    public ResponseCustom<?> downloadFile(@PathVariable Long fileId) {
        // 1. FileService를 통해 파일 다운로드 정보 가져오기
        FileDownloadResponse downloadDto = fileService.downloadFile(fileId);

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

        return ResponseCustom.OK()
                .headers(headers)
                .body(resource);
    }
}
