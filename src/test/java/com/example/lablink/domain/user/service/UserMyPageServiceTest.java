package com.example.lablink.domain.user.service;

import com.example.lablink.domain.user.dto.request.MyPageCheckRequestDto;
import com.example.lablink.domain.user.dto.response.UserModifyResponseDto;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.repository.UserRepository;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.global.exception.GlobalErrorCode;
import com.example.lablink.global.exception.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserMyPageServiceTest {

    @InjectMocks
    private UserMyPageService userMyPageService;
    @Mock
    private UserService userService;
    @Mock
    private UserModifyResponseDto userModifyResponseDto;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    UserDetailsImpl userDetails;

    MyPageCheckRequestDto myPageCheckRequestDto = new MyPageCheckRequestDto("inputPassword");

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {

        @Test
        @DisplayName("성공 - 비밀번호 확인")
        void checkUser() {
            // given
            User user = new User();
            String id = "1";
            UserDetailsImpl userDetails = new UserDetailsImpl(user, id);
            String myPassword = userDetails.getPassword();
            String inputPassword = "inputPassword";

            given(userService.getUser(userDetails)).willReturn(user);
//            given(myPageCheckRequestDto.getPassword()).willReturn(inputPassword);
            given(passwordEncoder.matches(inputPassword, myPassword)).willReturn(true);

            // when & then
            UserModifyResponseDto expectedResponseDto = new UserModifyResponseDto(user.getUserName(), user.getDateOfBirth());
            UserModifyResponseDto actualResponseDto = userMyPageService.checkUser(userDetails, myPageCheckRequestDto);
            assertEquals(expectedResponseDto.getUserName(), actualResponseDto.getUserName());
            assertEquals(expectedResponseDto.getDateOfBirth(), actualResponseDto.getDateOfBirth());
        }
        @Test
        @DisplayName("성공 - 유저 정보 수정")
        void modifyProfile() {
            User user = new User();
//            String id = "1";
//            UserDetailsImpl userDetails = new UserDetailsImpl(user, id);
            MyPageCheckRequestDto.UserModifyRequestDto checkRequestDto = new MyPageCheckRequestDto.UserModifyRequestDto();
            given(userDetails.getUser()).willReturn(user);

            // when
            userMyPageService.modifyProfile(userDetails, checkRequestDto);
            // then
            verify(userMapper).updateUserModifyDto(checkRequestDto, user);
            verify(userRepository).save(user);
        }
        @Test
        @DisplayName("성공 - 유저 비밀번호 변경")
        void changePassword() {
            // given
            User user = new User();
            String id = "1";
            UserDetailsImpl userDetails = new UserDetailsImpl(user, id);

            given(userService.getUser(userDetails)).willReturn(user);
            given(passwordEncoder.matches(myPageCheckRequestDto.getPassword(), user.getPassword())).willReturn(false);

            // when
            userMyPageService.changePassword(userDetails, myPageCheckRequestDto);
            // then
            verify(userRepository).save(user);
        }
    } // 성공 케이스


    @Nested
    @DisplayName("실패 케이스")
    class FailCase {
        @Test
        @DisplayName("실패 - 비밀번호 확인")
        void checkUserFail() {
            User user = new User();
            String myPassword = userDetails.getPassword();
            String inputPassword = "inputPassword";

            given(userService.getUser(userDetails)).willReturn(user);
            given(passwordEncoder.matches(inputPassword, myPassword)).willReturn(false);
            // when & then
            GlobalException exception = assertThrows(GlobalException.class, () -> userMyPageService.checkUser(userDetails, myPageCheckRequestDto),
                    GlobalErrorCode.PASSWORD_MISMATCH.getMessage());
            assertEquals(exception.getErrorCode(), GlobalErrorCode.PASSWORD_MISMATCH);
        }
//        @Test
//            @DisplayName("실패 - 유저 비밀번호 변경")
//            void changePasswordFail() {
//                // given
//                User user = new User();
//                String myPassword = userDetails.getPassword();
//                String inputPassword = "inputPassword";
//
//                given(userService.getUser(userDetails)).willReturn(user);
//                given(passwordEncoder.matches(inputPassword, myPassword)).willReturn(false);
//                // when
//                GlobalException exception = assertThrows(GlobalException.class, () -> userMyPageService.changePassword(userDetails, myPageCheckRequestDto), GlobalErrorCode.DUPLICATE_PASSWORD.getMessage());
//                // then
//                assertThrows(GlobalException.class, () -> userMyPageService.changePassword(userDetails, myPageCheckRequestDto), GlobalErrorCode.PASSWORD_MISMATCH.getMessage());
////                assertEquals(exception.getErrorCode(), GlobalErrorCode.DUPLICATE_PASSWORD);
//
//            }


    }






}