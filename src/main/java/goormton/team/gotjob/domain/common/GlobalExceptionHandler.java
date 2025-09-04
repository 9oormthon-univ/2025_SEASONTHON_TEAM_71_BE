package goormton.team.gotjob.domain.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<String>> handleApi(ApiException e){
        return ResponseEntity.status(e.getStatus()).body(new ApiResponse<>("ERROR", e.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleBadReq(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(new ApiResponse<>("ERROR", e.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleEtc(Exception e){
        return ResponseEntity.internalServerError().body(new ApiResponse<>("ERROR", e.getMessage()));
    }
}
