package com.example.lablink.global.message;
import com.example.lablink.domain.application.exception.ApplicationErrorCode;
import com.example.lablink.domain.company.exception.CompanyErrorCode;
import com.example.lablink.domain.feedback.exception.FeedBackErrorCode;
import com.example.lablink.domain.study.exception.StudyErrorCode;
import com.example.lablink.domain.user.exception.UserErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@AllArgsConstructor
public class ResponseMessage<T> {
    private final int statusCode;
    private final String message;
    private final T data;

    public static ResponseMessage ErrorResponse(int statusCode, String message) {
        return ResponseMessage.builder()
                        .statusCode(statusCode)
                        .message(message)
                        .data("")
                        .build();
    }

    public static ResponseEntity ErrorResponse(UserErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ResponseMessage.builder()
                        .statusCode(errorCode.getHttpStatus().value())
                        .message(errorCode.getMessage())
                        .data("")
                        .build()
                );
    }

    public static ResponseEntity ErrorResponse(CompanyErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ResponseMessage.builder()
                        .statusCode(errorCode.getHttpStatus().value())
                        .message(errorCode.getMessage())
                        .data("")
                        .build()
                );
    }

    // StudyErrorCode
    public static ResponseEntity ErrorResponse(StudyErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ResponseMessage.builder()
                        .statusCode(errorCode.getHttpStatus().value())
                        .message(errorCode.getMessage())
                        .data("")
                        .build()
                );
    }

    public static ResponseEntity ErrorResponse(ApplicationErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ResponseMessage.builder()
                        .statusCode(errorCode.getHttpStatus().value())
                        .message(errorCode.getMessage())
                        .data("")
                        .build()
                );

    }

    public static ResponseEntity ErrorResponse(FeedBackErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ResponseMessage.builder()
                        .statusCode(errorCode.getHttpStatus().value())
                        .message(errorCode.getMessage())
                        .data("")
                        .build()
                );

    }

    public static <T> ResponseEntity SuccessResponse(String message, T data){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseMessage.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(message)
                        .data(data)
                        .build()
                );
    }
}