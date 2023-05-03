package com.example.lablink.domain.company.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CompanyNameCheckRequestDto {

    @NotBlank(message = "기업명을 입력하세요.")
    private String companyName;
}
