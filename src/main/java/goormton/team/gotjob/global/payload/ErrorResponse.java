package goormton.team.gotjob.global.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Schema(description = "ApiResponse에서, Error에 대한 정보를 직접적으로 가지고 있는 response")
public class ErrorResponse {

    private final boolean check = false;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @JsonProperty("class")
    private final String clazz;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<ValidationError> errors;

    @Builder
    public ErrorResponse (HttpStatus httpStatus, String code, String message, String clazz, BindingResult bindingResult){
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.clazz = clazz;
        this.errors = ValidationError.of(bindingResult);
    }

    @Getter
    public static class ValidationError{
        private final String field;
        private final String value;
        private final String message;

        private ValidationError(FieldError fieldError){
            this.field = fieldError.getField();
            this.value = fieldError.getRejectedValue() == null? "" :fieldError.getRejectedValue().toString() ;
            this.message = fieldError.getDefaultMessage();
        }

        public static List<ValidationError> of(final BindingResult bindingResult){
            if (bindingResult == null) {
                return List.of(); // 또는 Collections.emptyList()
            }
            return bindingResult.getFieldErrors().stream()
                    .map(ValidationError::new)
                    .toList();
        }
    }
}
