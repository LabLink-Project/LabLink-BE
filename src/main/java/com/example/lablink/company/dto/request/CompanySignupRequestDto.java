package com.example.lablink.company.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanySignupRequestDto {

    @NotBlank(message = "이메일을 입력하세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "유효한 이메일 주소를 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 8, max = 20, message = "영문, 숫자, 특수문자를 포함한 8~20 글자를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[~!@#$%^&*()_+|<>?{}\\[\\]\\\\-])(?=\\S+$).{8,20}$", message = "영문, 숫자, 특수문자를 포함한 8~20 글자를 입력해 주세요.")
    private String password;

    @NotBlank(message = "회사명을 입력해 주세요.")
    private String companyName;

    @NotBlank(message = "대표자명을 입력해 주세요.")
    private String ownerName;

    @NotBlank(message = "관심 사업을 입력해 주세요.")
    private String business;

    @NotBlank(message = "담당자 연락처를 입력해 주세요.")
    private String managerPhone;

    @NotBlank(message = "회사 주소를 입력해 주세요.")
    private String address;
}
