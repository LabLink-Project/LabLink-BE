package com.example.lablink.study.exception;

import com.example.lablink.user.exception.UserErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyException extends RuntimeException {
    private final StudyErrorCode errorCode;
}
