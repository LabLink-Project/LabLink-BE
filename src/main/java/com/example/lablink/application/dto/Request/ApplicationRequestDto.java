package com.example.lablink.application.dto.Request;

import lombok.Getter;

import java.util.Date;

@Getter
public class ApplicationRequestDto {
    private String message;

    private String userName;

    private String userPhone;

    private String userAddress;

    private String userGender;

    private Date dateOfBirth;
}
