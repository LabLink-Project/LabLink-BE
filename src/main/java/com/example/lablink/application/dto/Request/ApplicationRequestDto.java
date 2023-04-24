package com.example.lablink.application.dto.Request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ApplicationRequestDto {
    private String message;

    private String userName;

    private String userPhone;

    private String userAddress;

    private String userGender;

    private LocalDate dateOfBirth;
}
