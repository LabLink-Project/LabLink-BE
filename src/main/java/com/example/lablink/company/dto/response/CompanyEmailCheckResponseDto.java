package com.example.lablink.company.dto.response;

import com.example.lablink.message.ResponseMessage;
import lombok.Getter;

@Getter
public class CompanyEmailCheckResponseDto<T> extends ResponseMessage {

    private int status;
    private String message;
    private T data;

    public CompanyEmailCheckResponseDto(int statusCode, String message, T data) {
        super(statusCode, message, data);
    }
}
