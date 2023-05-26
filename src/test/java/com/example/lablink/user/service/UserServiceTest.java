package com.example.lablink.user.service;

import com.example.lablink.domain.application.service.ApplicationService;
import com.example.lablink.domain.bookmark.service.BookmarkService;
import com.example.lablink.domain.company.entity.Company;
import com.example.lablink.domain.company.service.CompanyService;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.user.dto.response.MyLabResponseDto;
import com.example.lablink.domain.user.entity.*;
import com.example.lablink.domain.user.repository.RefreshTokenRepository;
import com.example.lablink.domain.user.service.TermsService;
import com.example.lablink.domain.user.service.UserInfoService;
import com.example.lablink.domain.user.service.UserService;
import com.example.lablink.global.common.dto.request.SignupEmailCheckRequestDto;
import com.example.lablink.global.exception.GlobalErrorCode;
import com.example.lablink.global.exception.GlobalException;
import com.example.lablink.global.jwt.JwtUtil;
import com.example.lablink.domain.user.dto.request.LoginRequestDto;
import com.example.lablink.domain.user.dto.request.SignupRequestDto;
import com.example.lablink.domain.user.dto.request.UserNickNameRequestDto;
import com.example.lablink.domain.user.repository.UserRepository;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    @Mock
    private Provider<CompanyService> companyServiceProvider; // Provider 타입의 모킹 필요
    @Mock
    private CompanyService companyService; // Provider 타입의 모킹 필요
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    SignupRequestDto signupRequestDto = new SignupRequestDto(
        "test01@naver.com",
        "nick",
        "password",
        "01011111111",
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

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {
        @Test
        @DisplayName("회원가입 성공")
        void signup() {
            // given
            User user = new User();
            Terms terms = new Terms();
            given(userInfoService.saveUserInfo(signupRequestDto)).willReturn(new UserInfo());
            given(userRepository.save(any(User.class))).willReturn(user);
            given(termsService.saveTerms(signupRequestDto, user)).willReturn(terms);
            given(companyServiceProvider.get()).willReturn(companyService);
            given(companyService.existEmail(any(String.class))).willReturn(false);
            // when
            String result = userService.signup(signupRequestDto);
            // then
            assertEquals("회원가입 완료.", result);
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
        @DisplayName("회원가입 - 이메일 사용 가능")
        void signup_can_use_email() {
            // given
            SignupEmailCheckRequestDto signupEmailCheckRequestDto = new SignupEmailCheckRequestDto("wrongTest01@naver.com");
            given(userRepository.existsByEmail(signupEmailCheckRequestDto.getEmail())).willReturn(false);
            given(companyServiceProvider.get()).willReturn(companyService);
            // when
            String result = userService.emailCheck(signupEmailCheckRequestDto);
            // then
            assertEquals("사용 가능합니다.", result);
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
//        @Test
//        @DisplayName("내 실험 관리 - 신청 목록")
//        void getMyLabs() {
//            // Given
//            User user = new User();
//            Study study1 = new Study();
//            Company company = new Company();
//            company.setCompanyName("Example Company");
//            study1.setCompany(company);
//
//            UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());
//            String approvalStatus = "";
//            String viewStatus = "";
//            Long id = 1L;
//
//            MyLabResponseDto myLab1 = new MyLabResponseDto(1L, "title", now(), 5000, "address",  viewStatus, approvalStatus, now(), "companyName");
//            List<MyLabResponseDto> myLabs = new ArrayList<>();
//            myLabs.add(myLab1);
//
//            TypedQuery<MyLabResponseDto> query = mock(TypedQuery.class);
//            given(em.createQuery(any(String.class), eq(MyLabResponseDto.class))).willReturn(query);
//            given(query.setParameter("user", userDetails.getUser())).willReturn(query);
//            given(query.getResultList()).willReturn(myLabs);
//
//            // When
//            List<MyLabResponseDto> myLabs1 = userService.getMyLabs(userDetails);
//
//            // Then
//            assertEquals(myLabs, myLabs1);
//        }


        @Test
        @DisplayName("유저 닉네임 찾기 메서드")
        void test() {
            // given
            User user = new User();
            given(userRepository.findByNickName(user.getNickName())).willReturn(Optional.of(user));
            // when
            User result = userService.getUserByNickname(user.getNickName());
            //then
            assertEquals(user, result);
        }
    } // class SuccessCase



    @Nested
    @DisplayName("실패 케이스")
    class FailCase {
        @Test
        @DisplayName("회원가입 실패 - 이메일 존재")
        void signup_fail_duplicateEmail() {
            // given
            given(userRepository.existsByEmail(signupRequestDto.getEmail())).willReturn(true);
            // when & then
            assertThrows(GlobalException.class,
                    () -> userService.signup(signupRequestDto), GlobalErrorCode.DUPLICATE_EMAIL.getMessage());
        }
        @Test
        @DisplayName("회원가입 실패 - 닉네임 존재")
        void signup_fail_duplicateNickName() {
            // given
            given(companyServiceProvider.get()).willReturn(companyService);
            given(userRepository.existsByNickName(signupRequestDto.getNickName())).willReturn(true);
            // when and then
            GlobalException exception = assertThrows(GlobalException.class, () -> userService.signup(signupRequestDto),
                    GlobalErrorCode.DUPLICATE_NICK_NAME.getMessage());
            assertEquals(GlobalErrorCode.DUPLICATE_NICK_NAME, exception.getErrorCode());
        }
        @Test
        @DisplayName("회원가입 실패 - 필수 약관 미동의")
        void signup_insufficientTermsAgreement() {
            // given
            given(companyServiceProvider.get()).willReturn(companyService);
            SignupRequestDto signupRequestDto2 = new SignupRequestDto(
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
            GlobalException exception = assertThrows(GlobalException.class, () -> userService.signup(signupRequestDto2),
                    GlobalErrorCode.NEED_AGREE_REQUIRE_TERMS.getMessage());
            // then
            assertEquals(GlobalErrorCode.NEED_AGREE_REQUIRE_TERMS, exception.getErrorCode());
        }
        @Test
        @DisplayName("이메일 중복 검사 실패 - 이메일 사용 불가")
        void email_check_duplicate_email_failure() {
            // given
            SignupEmailCheckRequestDto signupEmailCheckRequestDto = new SignupEmailCheckRequestDto("existingEmail@example.com");
            given(companyServiceProvider.get()).willReturn(companyService);
            given(companyService.existEmail(signupEmailCheckRequestDto.getEmail())).willReturn(true);
            // when and then
            GlobalException exception = assertThrows(GlobalException.class, () -> userService.emailCheck(signupEmailCheckRequestDto),
                    GlobalErrorCode.DUPLICATE_EMAIL.getMessage());
            assertEquals(GlobalErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());
        }
        @Test
        @DisplayName("회원가입 실패 - 닉네임 중복")
        void signup_nickNameDuplicationCheck() {
            // given
            UserNickNameRequestDto userNickNameRequestDto = new UserNickNameRequestDto("nick");
            given(userRepository.existsByNickName(userNickNameRequestDto.getNickName())).willReturn(true);

            // when & then
            assertThrows(GlobalException.class,
                    () -> userService.nickNameCheck(userNickNameRequestDto), GlobalErrorCode.DUPLICATE_NICK_NAME.getMessage());
        }
        @Test
        @DisplayName("회원가입 실패 - 인증 유저 있음")
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
        @DisplayName("로그인 실패 - 비밀번호 불일치")
        void login_fail_not_match() {
            // given
            User user = new User();
            HttpServletResponse response = new MockHttpServletResponse();
            given(userRepository.findByEmail(loginRequestDto.getEmail())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())).willReturn(false);
            // when & then
            assertThrows(GlobalException.class,
                    () -> userService.login(loginRequestDto, response), GlobalErrorCode.PASSWORD_MISMATCH.getMessage());
        }
        @Test
        @DisplayName("내 신청서 목록 조회 실패 - 비 로그인, 권한 없음")
        void getMyLabs_not_user() {
            // given
            UserDetailsImpl userDetails = null;
            // when & then
            assertThrows(GlobalException.class,
                    () -> userService.getMyLabs(userDetails), GlobalErrorCode.INVALID_TOKEN.getMessage());
        }
        @Test
        @DisplayName("유저 찾기 실패 - 인증 유저 없음")
        void not_auth_user() {
            // given
            User user = new User();
            UserDetailsImpl userDetails = new UserDetailsImpl(user, user.getEmail());
            given(userRepository.findById(any())).willReturn(Optional.empty());
            // when & then
            assertThrows(GlobalException.class,
                    () -> userService.getUser(userDetails), GlobalErrorCode.USER_NOT_FOUND.getMessage());
        }
        @Test
        @DisplayName("이메일 중복 검사 실패 - 중복 존재")
        void existEmail() {
            // Given
            String email = "existing@example.com";
            given(userRepository.existsByEmail(email)).willReturn(true);
            // When
            boolean emailExists = userService.existEmail(email);
            // Then
            assertTrue(emailExists, "Expected emailExists to be true");
            verify(userRepository).existsByEmail(email);
        }
//        @Test
//        @DisplayName("리프레쉬 토큰 발급")
//        void refreshAccessToken() {
//            // Given
//            User user = new User();
//            UserDetailsImpl userDetails = new UserDetailsImpl(user, "user@example.com");
//            HttpServletRequest request = mock(HttpServletRequest.class);
//            HttpServletResponse response = mock(HttpServletResponse.class);
//            Cookie[] cookies = new Cookie[1];
//            Cookie refreshTokenCookie = new Cookie("RefreshToken", "1234567890");
//            cookies[0] = refreshTokenCookie;
//            given(request.getCookies()).willReturn(cookies);
//
//            RefreshToken refreshToken = new RefreshToken();
//            refreshToken.setToken("refreshToken123");
//            refreshToken.setTokenIndex(1234567890L);
//            RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
//            given(refreshTokenRepository.findByTokenIndex(refreshToken.getTokenIndex())).willReturn(Optional.of(refreshToken));
//
//            UserService userService = new UserService(userRepository, passwordEncoder, termsService, jwtUtil, userInfoService, em, companyServiceProvider, refreshTokenRepository);
//
//            // When
//            GlobalException exception = assertThrows(GlobalException.class, () -> userService.refreshAccessToken(userDetails, request, response));
//
//            // Then
//            assertEquals(GlobalErrorCode.EXPIRED_REFRESH_TOKEN, exception.getErrorCode());
//            assertNotNull(exception.getMessage());
//        }

    } // class FailCase

}