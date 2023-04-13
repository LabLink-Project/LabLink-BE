package com.example.lablink.feedback.exception;


import com.example.lablink.message.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class FeedBackExceptionHandler {
    @ExceptionHandler(value = { FeedBackException.class })
    protected ResponseEntity<ResponseMessage> handleCustomException(FeedBackException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ResponseMessage.ErrorResponse(e.getErrorCode());
    }
}