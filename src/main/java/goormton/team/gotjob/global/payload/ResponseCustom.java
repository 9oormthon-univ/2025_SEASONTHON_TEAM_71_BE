package goormton.team.gotjob.global.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(description = "API 요청 성공시 리턴하는 response")
public class ResponseCustom<T>{

    private T data;
    private LocalDateTime transaction_time;
    private String description;
    private HttpStatus status;
    private int statusCode;

    @Builder
    public ResponseCustom(T data, LocalDateTime transaction_time, String description, HttpStatus status, int statusCode) {
        this.data = data;
        this.transaction_time = transaction_time;
        this.description = description;
        this.status = status;
        this.statusCode = statusCode;
    }

    public static <T> ResponseCustom<T> CREATED(@Nullable T data) {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .data(data)
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build();
    }

    public static <T> ResponseCustom<T> OK(@Nullable T data) {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .data(data)
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    public static <T> ResponseCustom<T> OK() {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    public static <T> ResponseCustom<T> BAD_REQUEST(@Nullable T data) {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .data(data)
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    public static <T> ResponseCustom<T> BAD_REQUEST(@Nullable String description) {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .description(description)
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    public static <T> ResponseCustom<T> NOT_FOUND(@Nullable T data) {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .data(data)
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
    }

    public static <T> ResponseCustom<T> NOT_FOUND(@Nullable String description) {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .description(description)
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
    }

    public static <T> ResponseCustom<T> FORBIDDEN() {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN)
                .statusCode(HttpStatus.FORBIDDEN.value())
                .build();
    }

    public static <T> ResponseCustom<T> FORBIDDEN(@Nullable String description) {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .description(description)
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();
    }

    public static <T> ResponseCustom<T> UNAUTHORIZED() {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED)
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
    }

    public static <T> ResponseCustom<T> INTERNAL_SERVER_ERROR(String description) {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .description(description)
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }

    public static <T> ResponseCustom<T> INTERNAL_SERVER_ERROR() {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}

