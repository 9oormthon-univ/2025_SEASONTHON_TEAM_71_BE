package goormton.team.gotjob.domain.consultant.dto;

public record KeywordMatchDto(
        Long consultantId,
        double score
) {
    public static KeywordMatchDto of(Long consultantId, double score) {
        return new KeywordMatchDto(consultantId, score);
    }
}
