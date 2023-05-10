package com.example.lablink.domain.user.service;

import com.example.lablink.domain.company.service.CompanyService;
import com.example.lablink.domain.user.dto.request.UserNickNameRequestDto;
import com.example.lablink.domain.user.entity.RefreshToken;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.entity.UserRoleEnum;
import com.example.lablink.domain.user.exception.UserException;
import com.example.lablink.domain.user.repository.RefreshTokenRepository;
import com.example.lablink.domain.user.repository.UserRepository;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.global.common.dto.request.SignupEmailCheckRequestDto;
import com.example.lablink.global.jwt.JwtUtil;

import com.example.lablink.domain.user.dto.request.LoginRequestDto;
import com.example.lablink.domain.user.dto.request.SignupRequestDto;
import com.example.lablink.domain.user.dto.response.MyLabResponseDto;

import com.example.lablink.domain.user.entity.UserInfo;
import com.example.lablink.domain.user.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class); // 메서드 실행 시간 측정 ex) 회원탈퇴
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TermsService termsService;
    private final JwtUtil jwtUtil;
    private final UserInfoService userInfoService;
    private final EntityManager em;
    private final @Lazy @Qualifier("companyService") Provider<CompanyService> companyServiceProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 유저 회원가입
    @Transactional
    public String signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 가입 이메일 중복 확인
        checkEmail(email);

        // 기업과 유저의 이메일 중복 확인
        CompanyService companyService = companyServiceProvider.get();
        if(companyService.existEmail(email)) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        // 가입 닉네임 중복 확인
        if (userRepository.existsByNickName(signupRequestDto.getNickName())) {
            throw new UserException(UserErrorCode.DUPLICATE_NICK_NAME);
        }

        // 필수 약관 동의
        if(!signupRequestDto.isAgeCheck() || !signupRequestDto.isTermsOfServiceAgreement() || !signupRequestDto.isPrivacyPolicyConsent() || !signupRequestDto.isSensitiveInfoConsent()) {
            throw new UserException(UserErrorCode.NEED_AGREE_REQUIRE_TERMS);
        }
        // 유저 저장 및 유저를 약관에 저장시킴 -> 약관을 유저에 저장시키면 유저를 불러올때마다 약관이 불려와 무거움
        // userinfo는 회원가입할 때 받지 않음.
        UserInfo userInfo = userInfoService.saveUserInfo(signupRequestDto);
        User user = userRepository.save(new User(signupRequestDto, password, userInfo, UserRoleEnum.USER));
        termsService.saveTerms(signupRequestDto, user);
        return "회원가입 완료.";
    }

    // 유저 로그인
    @Transactional
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // 이메일 존재, 일치 여부
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND));

        // 비밀번호 일치 여부
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
        }

        // Access token 생성 및 헤더에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createUserToken(user));

        // Refresh token 생성 및 저장
        Long refreshTokenId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        RefreshToken refreshToken = new RefreshToken(refreshTokenId, jwtUtil.createUserRfToken(user.getEmail(), user.getNickName()));
        refreshTokenRepository.save(refreshToken);

        // Refresh token을 쿠키에 저장
        String refreshTokenCookieValue = String.format("RefreshToken=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Lax",
            refreshTokenId, 7 * 24 * 60 * 60);

        response.addHeader("Set-Cookie", refreshTokenCookieValue);

        return "로그인 완료.";

        // CSRF, JWT토큰 생성
//        CsrfToken userCsrfToken = csrfTokenRepository.generateToken(request);
//        String userToken = jwtUtil.createUserToken(user);

        // 쿠키 생성 및 JWT토큰 추가
//        Cookie cookie = new Cookie("Authorization", userToken);
//        cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 (1일)
//        cookie.setPath("/"); // 전제api가 쿠키에 액세스 가능
//        cookie.setHttpOnly(true); // XSS공격 방지 (악성코드?)
////        cookie.setSecure(true); // HTTPS 사용 시 설정 (쿠키가 보안되지 않은 연결을 통해 전송되는 경우 탈취 방지)
//        response.addCookie(cookie);

        // 쿠키 생성 및 CSRF토큰 추가
//        Cookie csrfCookie = new Cookie("XSRF-TOKEN", userCsrfToken.getToken());
//        csrfCookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 (1일)
//        csrfCookie.setPath("/"); // 전제api가 쿠키에 액세스 가능
//        csrfCookie.setHttpOnly(true); // XSS공격 방지 (악성코드?)
////        csrfCookie.setSecure(true); // HTTPS 사용 시 설정 (쿠키가 보안되지 않은 연결을 통해 전송되는 경우 탈취 방지)
//        response.addCookie(csrfCookie);

//        // 세션 쿠키 생성 및 추가 > websecurity 수정 필요
//        HttpSession session = request.getSession(true);
//        session.setAttribute("Authorization", token);
    }

    // 유저 이메일 중복 체크
    @Transactional(readOnly = true)
    public String emailCheck(SignupEmailCheckRequestDto signupEmailCheckRequestDto) {
        if(userRepository.existsByEmail(signupEmailCheckRequestDto.getEmail())) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        CompanyService companyService = companyServiceProvider.get();
        if(companyService.existEmail(signupEmailCheckRequestDto.getEmail())) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }
        return "사용 가능합니다.";
    }

    // 유저 닉네임 중복 확인
    public String nickNameCheck(UserNickNameRequestDto userNickNameRequestDto) {
        if(userRepository.existsByNickName(userNickNameRequestDto.getNickName())) {
            throw new UserException(UserErrorCode.DUPLICATE_NICK_NAME);
        }
        return "사용 가능합니다.";
    }

    // 인증 유저 가져오기
    public User getUser(UserDetailsImpl userDetails){
        return  userRepository.findById(userDetails.getUser().getId()).orElseThrow(
            ()->new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    // 회원 탈퇴
    @Transactional
    public String deleteUser(UserDetailsImpl userDetails, HttpServletResponse response) {
        long start = System.currentTimeMillis();

        // 삭제 & 로그아웃 (헤더 null값 만들기)
        userRepository.deleteUserAndData(userDetails.getUser().getId());
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, null);

        long end = System.currentTimeMillis();
        logger.info("deleteUser took {} ms", end - start);
        return "탈퇴 완료.";
    }

    // 내 실험 관리 - 신청한 목록
    // TODO User 권한 없으면 한번에 예외 발생 처리 - 현재 각 메서드별 처리
    @Transactional
    public List<MyLabResponseDto> getMyLabs(UserDetailsImpl userDetails) {
        long start = System.currentTimeMillis();

        if(userDetails == null || userDetails.equals(" ")) {
            throw new UserException(UserErrorCode.INVALID_TOKEN);
        }

        // 내가 신청한 목록
        TypedQuery<MyLabResponseDto> query = em.createQuery(
            "SELECT new com.example.lablink.domain.user.dto.response.MyLabResponseDto(s, a.applicationViewStatusEnum, a.approvalStatusEnum) " +
                "FROM Study s INNER JOIN Application a ON s.id = a.studyId " +
                "WHERE a.user = :user", MyLabResponseDto.class);
        query.setParameter("user", userDetails.getUser());

        long end = System.currentTimeMillis();
        logger.info("getMyLabs took {} ms", end - start);

        return query.getResultList();
    }

    // 리프레시토큰 발급 메서드
    public String refreshAccessToken(UserDetailsImpl userDetails, HttpServletRequest request, HttpServletResponse response) {
        log.info("========================= 리프레시토큰 발급 시작");
        User user = getUser(userDetails);

        // 클라이언트가 주는 모든 쿠키 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new UserException(UserErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        // 쿠키 중 RefreshToken 가져오기
        Long refreshTokenIndex = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("RefreshToken")) {
                refreshTokenIndex = Long.parseLong(cookie.getValue());
                break;
            }
        }

        if (refreshTokenIndex == null) {
            throw new UserException(UserErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByTokenIndex(refreshTokenIndex).orElseThrow(
            () -> new UserException(UserErrorCode.EXPIRED_REFRESH_TOKEN));

        log.info("========================= 리프레시토큰 : {}", refreshToken.getToken());
        String token = jwtUtil.createUserToken(user);
        log.info("========================= 새 액세스토큰 : {}", token);

        return token;
    }

    public User getUserByNickname(String nickName) {
        return userRepository.findByNickName(nickName).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    // 가입 이메일 중복 확인
    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }
    }
    public boolean existEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}