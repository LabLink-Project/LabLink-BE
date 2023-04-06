package com.example.lablink.message;
import com.example.lablink.company.exception.CompanyErrorCode;
import com.example.lablink.category.exception.CategoryErrorCode;
import com.example.lablink.study.exception.StudyErrorCode;
import com.example.lablink.user.exception.UserErrorCode;
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

    public static ResponseEntity ErrorResponse(CategoryErrorCode errorCode) {
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