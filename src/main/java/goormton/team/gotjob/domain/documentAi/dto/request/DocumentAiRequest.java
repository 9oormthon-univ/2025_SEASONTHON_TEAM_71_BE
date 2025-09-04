package goormton.team.gotjob.domain.documentAi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "이력서 요약 api request dto")
public record DocumentAiRequest(
        MultipartFile resumeFile,
        String lang,
        Integer maxWords
) {
}
