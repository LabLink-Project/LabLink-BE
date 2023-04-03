package com.example.lablink.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserException extends RuntimeException {
    private final UserErrorCode errorCode;

}