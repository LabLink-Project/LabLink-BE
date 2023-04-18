package com.example.lablink.user.service;

import com.example.lablink.user.dto.request.SignupRequestDto;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.entity.UserInfo;
import com.example.lablink.user.entity.UserRoleEnum;
import com.example.lablink.user.exception.UserErrorCode;
import com.example.lablink.user.exception.UserException;
import com.example.lablink.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    // 임시의 Mock 객체를 생성할 때 import가 자동으로해당 service안에 있는 클래스들의 import? 의존성을 알아서 주입해준다 ??
    // 테스트 코드를 작성할 Service 주입.
    // setup메서드를 작성하지 않아도 된다
    // setup메서드는 Mock객체를 인스턴스화 시키는 것 ?
    @InjectMocks
    private UserService userService;
    // Mock으로 UserService에서 사용되는 클래스를 의존성 주입함.
    // 테스트에서는 UserService의 JwtUtill과 같은 클래스를 주입받을 필요는 없다.
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TermsService termsService;
    @Mock
    private UserInfoService userInfoService;


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
    @DisplayName("회원가입 성공")
    void signup() {
        // given
        User user = new User(signupRequestDto, passwordEncoder.encode(signupRequestDto.getPassword()), new UserInfo(), UserRoleEnum.USER);
        given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        String result = userService.signup(signupRequestDto);

        // then
        assertEquals("회원가입 완료.", result);
    }

    @Test
    @DisplayName("이메일 중복")
    void signup_emailDuplicationCheck() {
        // given
        given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(true);

        // when & then
        assertThrows(UserException.class,
            () -> userService.signup(signupRequestDto), UserErrorCode.DUPLICATE_EMAIL.getMessage());
    }

    @Test
    @DisplayName("필수 약관 체크")
    void signup_insufficientTermsAgreement() {
        // given
        UserInfo userInfo = userInfoService.saveUserInfo(signupRequestDto);
        User user = userRepository.save(new User(signupRequestDto, signupRequestDto.getPassword(), userInfo, UserRoleEnum.USER));
        given(termsService.saveTerms(signupRequestDto, user)).willThrow(new UserException(UserErrorCode.NEED_AGREE_REQUIRE_TERMS));

        // when & then
        assertThrows(UserException.class,
            () -> userService.signup(signupRequestDto), UserErrorCode.NEED_AGREE_REQUIRE_TERMS.getMessage());
    }
}
// given 각 메서드가 예상한 값을 반환하도록 설정.
// when userService.signup 메서드 호출
// 메서드가 호출되었는지 확인