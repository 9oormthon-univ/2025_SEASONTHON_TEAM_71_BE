package goormton.team.gotjob.domain.documentAi.dto.response;

import goormton.team.gotjob.domain.keword.domain.Keyword;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "키워드 추출 api response dto")
public record DocumentAiKeywordsResponse(
        List<Keyword> preferredJob,
        List<Keyword> skillsAndSpecs
) {
}
