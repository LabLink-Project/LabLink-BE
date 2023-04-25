package com.example.lablink.application.dto.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class ApplicationRequestDto {
    private String message;

    private String userName;

    private String userPhone;

    private String userAddress;

    private String userGender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

}
