package com.example.lablink.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatException extends RuntimeException {
    private final ChatErrorCode errorCode;
}