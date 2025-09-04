package goormton.team.gotjob.domain.documentAi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "키워드 추출 api response dto")
public record DocumentAiKeywordsResponse(
        String term,
        Integer weight
) {
}
