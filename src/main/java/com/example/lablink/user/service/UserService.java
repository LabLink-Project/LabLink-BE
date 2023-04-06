package com.example.lablink.user.service;

import com.example.lablink.company.exception.CompanyErrorCode;
import com.example.lablink.company.exception.CompanyException;
import com.example.lablink.jwt.JwtUtil;
import com.example.lablink.user.dto.request.LoginRequestDto;
import com.example.lablink.user.dto.request.SignupRequestDto;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.exception.UserErrorCode;
import com.example.lablink.user.exception.UserException;
import com.example.lablink.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TermsService termsService;
    private final JwtUtil jwtUtil;

    // 유저 회원가입
    public String signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        //이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new CompanyException(CompanyErrorCode.DUPLICATE_EMAIL);
        }

        // 유저 저장 및 유저를 약관에 저장시킴 -> 약관을 유저에 저장시키면 유저를 불러올때마다 약관이 불려와 무거움
        User user = userRepository.save(new User(password, signupRequestDto));
        termsService.saveTerms(signupRequestDto, user);
        return "회원가입 완료.";
    }

    // 유저 로그인
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response, HttpServletRequest request) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // 이메일 존재, 일치 여부
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND));

        // 비밀번호 일치 여부
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
        }

        // JWT 토큰 생성
        String token = jwtUtil.createUserToken(user);

        // 쿠키 생성 및 추가
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 설정 (1일)
        cookie.setHttpOnly(true); // XSS 방지를 위해 HttpOnly 속성 설정
        response.addCookie(cookie);
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createUserToken(user));
    }

    // 유저 이메일 찾기
    public User findByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return user;
    }
}
