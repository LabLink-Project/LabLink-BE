package com.example.lablink.domain.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {
    private final ApplicationErrorCode errorCode;

}