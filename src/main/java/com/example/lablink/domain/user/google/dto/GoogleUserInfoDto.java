package com.example.lablink.domain.user.google.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoogleUserInfoDto {
    private Long id;
    private String email;
    private String nickname;

    public GoogleUserInfoDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}