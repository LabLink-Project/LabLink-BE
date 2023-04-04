package com.example.lablink.company.exception;


import com.example.lablink.message.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CompanyExceptionHandler {
    @ExceptionHandler(value = { CompanyException.class })
    protected ResponseEntity<ResponseMessage> handleCustomException(CompanyException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ResponseMessage.ErrorResponse(e.getErrorCode());
    }
}