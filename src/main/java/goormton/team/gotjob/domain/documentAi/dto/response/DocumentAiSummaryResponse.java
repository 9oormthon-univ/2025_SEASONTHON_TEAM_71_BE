package goormton.team.gotjob.domain.documentAi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record DocumentAiSummaryResponse(
        String summary
) {
}
