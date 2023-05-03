package com.example.lablink.domain.feedback.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedBackException extends RuntimeException {
    private final FeedBackErrorCode errorCode;

}