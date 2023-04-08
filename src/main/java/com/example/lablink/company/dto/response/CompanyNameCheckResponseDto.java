package com.example.lablink.company.dto.response;

import com.example.lablink.message.ResponseMessage;
import lombok.Getter;

@Getter
public class CompanyNameCheckResponseDto <T> extends ResponseMessage {

    private int status;
    private String message;
    private T data;

    public CompanyNameCheckResponseDto(int statusCode, String message, T data) {
        super(statusCode, message, data);
    }
}
