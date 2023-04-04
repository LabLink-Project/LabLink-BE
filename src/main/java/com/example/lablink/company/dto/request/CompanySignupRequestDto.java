package com.example.lablink.company.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanySignupRequestDto {

    private String email;
    private String password;
    private String companyName;
    private String ownerName;
    private String business;
    private String managerPhone;
    private String address;
}
