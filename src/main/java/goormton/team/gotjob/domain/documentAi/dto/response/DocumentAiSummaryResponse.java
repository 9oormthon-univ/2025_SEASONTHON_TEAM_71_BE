package goormton.team.gotjob.domain.documentAi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이력서 요약 api response dto")
public record DocumentAiSummaryResponse(
        String summary
) {
}
