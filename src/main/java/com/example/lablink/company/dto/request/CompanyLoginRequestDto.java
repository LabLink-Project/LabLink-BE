package com.example.lablink.company.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyLoginRequestDto {

    private String email;
    private String password;
}
