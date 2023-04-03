package com.example.hhproject0.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequestDto {

    // 유효성 검사
//    @NotBlank
    private String email;
    private String password;
    private String userName;
    private String userPhone;
}
