package goormton.team.gotjob.global.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Schema(description = "API 요청 중 에러 발생시 리턴되는 response")
public class ApiResponse {

    @Schema(type = "boolean", example = "true", description = "올바르게 로직을 처리했으면 True, 아니면 False 반환.")
    private boolean check;
    @Schema(type = "object", example = "information", description = "restful의 정보를 감싸 표현. object 형식으로 표현.")
    private Object information;

    @Builder
    public ApiResponse(boolean check, Object information) {
        this.check = check;
        this.information = information;
    }

}
