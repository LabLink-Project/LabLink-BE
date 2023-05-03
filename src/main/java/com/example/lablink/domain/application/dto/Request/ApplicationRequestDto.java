package com.example.lablink.domain.application.dto.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ApplicationRequestDto {
    private String message;

    private String userName;

    private String userPhone;

    private String userAddress;

    private String userDetailAddress;

    private String userGender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

}
