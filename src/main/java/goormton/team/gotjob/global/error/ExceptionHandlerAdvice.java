package goormton.team.gotjob.global.error;

import goormton.team.gotjob.global.payload.ApiResponse;
import goormton.team.gotjob.global.payload.ErrorCode;
import goormton.team.gotjob.global.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionHandlerAdvice {

    // api 관련 예외에 대한 핸들링
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
//        final ErrorResponse response = ErrorResponse.builder()
//                .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
//                .code(e.getMessage())
//                .clazz(e.getMethod())
//                .message(e.getMessage())
//                .build();
//        ApiResponse apiResponse = ApiResponse.builder().check(false).information(response).build();
//        return new ResponseEntity<>(apiResponse, HttpStatus.METHOD_NOT_ALLOWED);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .code(e.getMessage())
                .clazz(e.getBindingResult().getObjectName())
                .message(e.toString())
                .bindingResult(e.getBindingResult())
                .build();
        ApiResponse apiResponse = ApiResponse.builder().check(false).information(response).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> handleInvalidParameterException(InvalidParameterException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .code(e.getMessage())
                .clazz(e.getErrors().getObjectName())
                .message(e.toString())
                .build();
        ApiResponse apiResponse = ApiResponse.builder().check(false).information(response).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    //    자바 자체 내부 예외에 관한 핸들링
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.toString())
                .build();
        ApiResponse apiResponse = ApiResponse.builder().check(false).information(response).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DefaultException.class)
    protected ResponseEntity<?> handleDefaultExeption(DefaultException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(errorCode.getHttpStatus())
                .code(errorCode.getCode())
                .message(errorCode.toString())
                .build();
        ApiResponse apiResponse = ApiResponse.builder().check(false).information(response).build();
        return new ResponseEntity<>(apiResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        ErrorCode errorCode = ErrorCode.INVALID_AUTHENTICATION;
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(errorCode.getHttpStatus())
                .code(errorCode.getCode())
                .message(e.toString())
                .build();
        ApiResponse apiResponse = ApiResponse.builder().check(false).information(response).build();
        return new ResponseEntity<>(apiResponse, errorCode.getHttpStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
                .message(e.getMessage())
                .build();
        ApiResponse apiResponse = ApiResponse.builder().check(false).information(response).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DefaultAuthenticationException.class)
    protected ResponseEntity<?> handleDefaultAuthenticationException(DefaultAuthenticationException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
                .message(e.getMessage())
                .build();
        ApiResponse apiResponse = ApiResponse.builder().check(false).information(response).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DefaultNullPointerException.class)
    protected ResponseEntity<?> handleDefaultNullPointerException(NullPointerException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
        ApiResponse apiResponse = ApiResponse.builder().check(false).information(response).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
        ApiResponse apiResponse = ApiResponse.builder().check(false).information(response).build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
