package com.example.lablink.domain.company.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyException extends RuntimeException {
    private final CompanyErrorCode errorCode;
}