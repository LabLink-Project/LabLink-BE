package com.example.lablink.domain.user.service;

import com.example.lablink.domain.user.dto.request.MyPageCheckRequestDto;
import com.example.lablink.domain.user.dto.response.UserModifyResponseDto;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.repository.UserRepository;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserMyPageServiceTest {

    @InjectMocks
    private UserMyPageService userMyPageService;
    @Mock
    private UserService userService;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private MyPageCheckRequestDto myPageCheckRequestDto;
    @Mock
    private UserModifyResponseDto userModifyResponseDto;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {

        @Test
        @DisplayName("성공 - 비밀번호 확인")
        void checkUser() {
            // given
            User user = new User();
            String myPassword = userDetails.getPassword();
            String inputPassword = "inputPassword";

            given(userService.getUser(userDetails)).willReturn(user);
            given(myPageCheckRequestDto.getPassword()).willReturn(inputPassword);
            given(passwordEncoder.matches(inputPassword, myPassword)).willReturn(true);

            // when & then
            UserModifyResponseDto expectedResponseDto = new UserModifyResponseDto(user.getUserName(), user.getDateOfBirth());
            UserModifyResponseDto actualResponseDto = userMyPageService.checkUser(userDetails, myPageCheckRequestDto);
            assertEquals(expectedResponseDto.getUserName(), actualResponseDto.getUserName());
            assertEquals(expectedResponseDto.getDateOfBirth(), actualResponseDto.getDateOfBirth());
        }





    } // 성공 케이스



    @Test
    void modifyProfile() {
    }

    @Test
    void changePassword() {
    }
}