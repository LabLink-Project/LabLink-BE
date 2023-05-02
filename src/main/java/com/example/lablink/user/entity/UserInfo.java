package com.example.lablink.user.entity;

import com.example.lablink.application.dto.Request.ApplicationRequestDto;
import com.example.lablink.user.dto.request.SignupRequestDto;
import com.example.lablink.user.kakao.dto.KakaoUserInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String userAddress;

    @Column(nullable = true)
    private String userDetailAddress;

    // xxx : 카카오로그인 phone 번호 못 받아서 true로 바꿈니다 ㅠ
    @Column(nullable = true)
    private String userPhone;

    public UserInfo(SignupRequestDto signupRequestDto) {
        this.userPhone = signupRequestDto.getUserPhone();
    }

    public UserInfo(KakaoUserInfoDto kakaoUserInfoDto) {
    }

    public void updateUserInfo(ApplicationRequestDto applicationRequestDto) {
        this.userAddress = applicationRequestDto.getUserAddress();
        this.userDetailAddress = applicationRequestDto.getUserDetailAddress();
    }
}
