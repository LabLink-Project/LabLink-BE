package com.example.lablink.domain.user.service;

import com.example.lablink.domain.user.entity.UserInfo;
import com.example.lablink.domain.user.kakao.dto.KakaoUserInfoDto;
import com.example.lablink.domain.user.repository.UserInfoRepository;
import com.example.lablink.domain.user.dto.request.SignupRequestDto;
import com.example.lablink.global.exception.GlobalErrorCode;
import com.example.lablink.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;

    // 회원가입시 UserInfo DB에 저장
    public UserInfo saveUserInfo(SignupRequestDto signupRequestDto) {
        return userInfoRepository.save(new UserInfo(signupRequestDto));
    }

    public UserInfo saveKakaoUserInfo(KakaoUserInfoDto kakaoUserInfoDto){
        return userInfoRepository.save(new UserInfo(kakaoUserInfoDto));
    }

    // UserInfo 찾기
    public UserInfo findUserInfo(Long userInfoId) {
        return userInfoRepository.findById(userInfoId).orElseThrow(() -> new GlobalException(GlobalErrorCode.USERINFO_NOT_FOUND));
    }
}
