package com.example.lablink.domain.user.service;

import com.example.lablink.domain.user.dto.request.SignupRequestDto;
import com.example.lablink.domain.user.entity.UserInfo;
import com.example.lablink.domain.user.kakao.dto.KakaoUserInfoDto;
import com.example.lablink.domain.user.repository.UserInfoRepository;
import com.example.lablink.domain.user.service.UserInfoService;
import com.example.lablink.global.exception.GlobalErrorCode;
import com.example.lablink.global.exception.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserInfoRepository userInfoRepository;
    @Mock
    private KakaoUserInfoDto kakaoUserInfoDto;

    private SignupRequestDto signupRequestDto = new SignupRequestDto(
        "test01@naver.com",
        "nick",
        "password",
        "010-1111-1111",
        true,
        true,
        true,
        true,
        false
    );

    @Test
    @DisplayName("회원가입시 info 저장")
    void saveUserInfo() {
        // given
        UserInfo userInfo = new UserInfo();
        given(userInfoRepository.save(any(UserInfo.class))).willReturn(userInfo);
        // when
        UserInfo result = userInfoService.saveUserInfo(signupRequestDto);
        // then
        assertEquals(userInfo, result);
    }
    @Test
        @DisplayName("소셜 유저 info 저장")
        void saveKakaoUserInfo() {
        // given
        UserInfo userInfo = new UserInfo();
        given(userInfoRepository.save(any(UserInfo.class))).willReturn(userInfo);
        // when
        UserInfo result = userInfoService.saveKakaoUserInfo(kakaoUserInfoDto);
        // then
        assertEquals(userInfo, result);
        }
    @Test
    @DisplayName("userInfo 찾기")
    void findUserInfo() {
        // given
        UserInfo userInfo = new UserInfo();
        given(userInfoRepository.findById(userInfo.getId())).willThrow(GlobalException.class);
        // when & then
        assertThrows(GlobalException.class,
        () -> userInfoService.findUserInfo(userInfo.getId()), GlobalErrorCode.USER_NOT_FOUND.getMessage());
    }

}