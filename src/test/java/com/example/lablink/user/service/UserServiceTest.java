package com.example.lablink.user.service;

import com.example.lablink.domain.application.service.ApplicationService;
import com.example.lablink.domain.bookmark.service.BookmarkService;
import com.example.lablink.domain.user.service.TermsService;
import com.example.lablink.domain.user.service.UserInfoService;
import com.example.lablink.domain.user.service.UserService;
import com.example.lablink.global.jwt.JwtUtil;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.user.dto.request.LoginRequestDto;
import com.example.lablink.domain.user.dto.request.SignupRequestDto;
import com.example.lablink.domain.user.dto.request.UserEmailCheckRequestDto;
import com.example.lablink.domain.user.dto.request.UserNickNameRequestDto;
import com.example.lablink.domain.user.dto.response.MyLabResponseDto;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.exception.UserErrorCode;
import com.example.lablink.domain.user.exception.UserException;
import com.example.lablink.domain.user.repository.UserRepository;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    private BookmarkService bookmarkService;
    @Mock
    private ApplicationService applicationService;
    @Mock
    private EntityManager em;
    @Mock
    private TermsService termsService;
    @Mock
    private UserInfoService userInfoService;
    @Mock
    private JwtUtil jwtUtil;

    SignupRequestDto signupRequestDto = new SignupRequestDto(
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

    LoginRequestDto loginRequestDto = new LoginRequestDto(
        "test01@naver.com",
        "password"
    );

    @Test
    @DisplayName("회원가입 성공")
    void signup() {
        // given
        User user = new User();
        given(userRepository.save(any(User.class))).willReturn(user);
        // when
        String result = userService.signup(signupRequestDto);

        // then
        assertEquals("회원가입 완료.", result);
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 존재")
    void signup_fail_duplicateEmail() {
        // given
        given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(true);

        // when & then
        assertThrows(UserException.class,
            () -> userService.signup(signupRequestDto), UserErrorCode.DUPLICATE_EMAIL.getMessage());
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 존재")
    void signup_fail_duplicateNickName() {
        // given
        given(userRepository.existsByNickName(signupRequestDto.getNickName())).willReturn(true);

        // when & then
        assertThrows(UserException.class,
            () -> userService.signup(signupRequestDto), UserErrorCode.DUPLICATE_NICK_NAME.getMessage());
    }

    @Test
    @DisplayName("회원가입 실패 - 필수 약관 미동의")
    void signup_insufficientTermsAgreement() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto(
            "test01@naver.com",
            "nick",
            "password",
            "010-1111-1111",
            false,
            true,
            true,
            true,
            false
        );

        // when & then
        assertThrows(UserException.class,
            () -> userService.signup(signupRequestDto), UserErrorCode.NEED_AGREE_REQUIRE_TERMS.getMessage());
    }

    @Test
    @DisplayName("로그인 성공")
    void login() {
        // given
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        User user = new User();
        HttpServletResponse response = new MockHttpServletResponse();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(password, user.getPassword())).willReturn(true);

        // when
        String result = userService.login(loginRequestDto, response);

        // then
        assertEquals("로그인 완료.", result);
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_fail_not_match() {
        // given
        User user = new User();
        HttpServletResponse response = new MockHttpServletResponse();

        given(userRepository.findByEmail(loginRequestDto.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).willReturn(false);

        // when & then
        assertThrows(UserException.class,
            () -> userService.login(loginRequestDto, response), UserErrorCode.PASSWORD_MISMATCH.getMessage());
    }

    @Test
    @DisplayName("회원가입 - 이메일 사용 가능")
    void signup_can_use_email() {
        // given
        UserEmailCheckRequestDto userEmailRequestDto = new UserEmailCheckRequestDto("wrongTest01@naver.com");
        given(userRepository.existsByEmail(userEmailRequestDto.getEmail())).willReturn(false);
        // when
        String result = userService.emailCheck(userEmailRequestDto);
        // then
        assertEquals("사용 가능합니다.", result);
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 사용 불가")
    void signup_emailDuplicationCheck() {
        // given
        UserEmailCheckRequestDto userEmailRequestDto = new UserEmailCheckRequestDto("wrongTest01@naver.com");
        given(userRepository.existsByEmail(userEmailRequestDto.getEmail())).willReturn(true);

        // when & then
        assertThrows(UserException.class,
            () -> userService.emailCheck(userEmailRequestDto), UserErrorCode.DUPLICATE_EMAIL.getMessage());
    }

    @Test
    @DisplayName("회원가입 - 닉네임 사용 가능")
    void signup_can_use_nickName() {
        // given
        UserNickNameRequestDto userNickNameRequestDto = new UserNickNameRequestDto("nick");
        given(userRepository.existsByNickName(userNickNameRequestDto.getNickName())).willReturn(false);

        // when
        String result = userService.nickNameCheck(userNickNameRequestDto);
        // then
        assertEquals("사용 가능합니다.", result);
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void signup_nickNameDuplicationCheck() {
        // given
        UserNickNameRequestDto userNickNameRequestDto = new UserNickNameRequestDto("nick");
        given(userRepository.existsByNickName(userNickNameRequestDto.getNickName())).willReturn(true);

        // when & then
        assertThrows(UserException.class,
            () -> userService.nickNameCheck(userNickNameRequestDto), UserErrorCode.DUPLICATE_NICK_NAME.getMessage());
    }

    @Test
    @DisplayName("인증 유저 있음")
    void auth_user() {
        // given
        User user = new User();
        UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        // when
        User result = userService.getUser(userDetails);
        // then
        assertEquals(user, result);
    }

    @Test
    @DisplayName("인증 유저 없음")
    void not_auth_user() {
        // given
        User user = new User();
        UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());
        given(userRepository.findById(any())).willReturn(Optional.empty());
        // when & then
        assertThrows(UserException.class,
            () -> userService.getUser(userDetails), UserErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteUser() {
        // given
        // new HttpServletResponse();가 아닌 동적 객체 생성?
        HttpServletResponse response = mock(HttpServletResponse.class);
        User user = new User();
        UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());

        // when
        userService.deleteUser(userDetails, response);

        // then
        verify(userRepository).deleteUserAndData(user.getId());
        verify(response).setHeader(JwtUtil.AUTHORIZATION_HEADER, null);

    }

//    @Test
//    @DisplayName("내 실험 관리 - 신청 목록")
//    void getMyLabs() {
//        // given
//        User user = new User();
//
//        UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());
//        String approvalStatus = "";
//        String viewStatus = "";
//        Long id = 1L;
//
//        MyLabResponseDto myLab1 = new MyLabResponseDto(study, id, viewStatus, approvalStatus);
//        List<MyLabResponseDto> myLabs = new ArrayList<>();
//        myLabs.add(myLab1);
//
//        TypedQuery<MyLabResponseDto> query = mock(TypedQuery.class);
//        given(em.createQuery(any(String.class), eq(MyLabResponseDto.class))).willReturn(query);
//        given(query.setParameter("user", userDetails.getUser())).willReturn(query);
//        given(query.getResultList()).willReturn(myLabs);
//        // when
//        List<MyLabResponseDto> myLabs1 = userService.getMyLabs(userDetails);
//
//        //then
//        assertEquals(myLabs, myLabs1);
//    }

    @Test
    @DisplayName("내 실험 관리 - 비 로그인 권한 없음")
    void getMyLabs_not_user() {
        // given
        UserDetailsImpl userDetails = null;
        // when & then
        assertThrows(UserException.class,
            () -> userService.getMyLabs(userDetails), UserErrorCode.INVALID_TOKEN.getMessage());

    }

    @Test
    @DisplayName("유저 찾기 메서드")
    void test() {
        // given
        User user = new User();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        // when
        User result = userService.findUser(user.getId());
        //then
        assertEquals(user, result);
    }

}