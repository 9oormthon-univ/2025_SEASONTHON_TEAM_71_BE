package goormton.team.gotjob.domain.documentAi.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "DocumentAI 서비스 호출 API", description = "유저가 pdf 파일 형태로 올린 이력서를 입력받아 요약 및 키워드 추출 등의 서비스를 실행하는 controller입니다.")
@RestController
@RequiredArgsConstructor
public class DocumentAiController {

//    @Operation(summary = "회원가입 후 로그인", description = "회원가입과 로그인을 수행합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공", content = {
//                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponse.class))
//            }),
//            @ApiResponse(responseCode = "400", description = "실패")
//    })
//    @PostMapping("/summary")
//    public
}
