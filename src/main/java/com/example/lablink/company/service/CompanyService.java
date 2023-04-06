package com.example.lablink.company.service;

import com.example.lablink.company.dto.request.CompanyLoginRequestDto;
import com.example.lablink.company.dto.request.CompanySignupRequestDto;
import com.example.lablink.company.entity.Company;
import com.example.lablink.company.exception.CompanyErrorCode;
import com.example.lablink.company.exception.CompanyException;
import com.example.lablink.company.repository.CompanyRepository;
import com.example.lablink.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
//    private final CsrfTokenRepository csrfTokenRepository;

    // 기업 회원가입
    public String companySignup(CompanySignupRequestDto companySignupRequestDto) {
//        UserRoleEnum role = UserRoleEnum.BUSINESS;
        String email = companySignupRequestDto.getEmail();
        String password = passwordEncoder.encode(companySignupRequestDto.getPassword());

        //이메일 중복 확인
        if (companyRepository.existsByEmail(email)) {
            throw new CompanyException(CompanyErrorCode.DUPLICATE_EMAIL);
        }

        companyRepository.save(new Company(password, companySignupRequestDto));
        return "회원가입 완료";
    }

    // 기업 로그인
    public void companyLogin(CompanyLoginRequestDto companyLoginRequestDto, HttpServletResponse response, HttpServletRequest request) {
        String email = companyLoginRequestDto.getEmail();
        String password = passwordEncoder.encode(companyLoginRequestDto.getPassword());

        // 로그인시 작성 email과 db의 email의 일치, 존재 확인
        Company company = companyRepository.findByEmail(email).orElseThrow(() -> new CompanyException(CompanyErrorCode.EMAIL_NOT_FOUND));

        // 비밀번호 일치 여부
        if (!passwordEncoder.matches(password, company.getPassword())) {
            throw new CompanyException(CompanyErrorCode.PASSWORD_MISMATCH);
        }

//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createCompanyToken(company));

        // 1 생성
//        CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
//
//        // CSRF 토큰 쿠키 생성 및 추가
//        Cookie csrfCookie = new Cookie("XSRF-TOKEN", csrfToken.getToken());
//        csrfCookie.setPath("/");
//        csrfCookie.setHttpOnly(true);
//        response.addCookie(csrfCookie);

        // JWT 토큰 생성
        String token = jwtUtil.createCompanyToken(company);

        // 쿠키 생성 및 추가
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 설정 (1일)
        cookie.setHttpOnly(true); // XSS 방지를 위해 HttpOnly 속성 설정
        response.addCookie(cookie);
    }

    // 기업 이메일 찾기
    public Company findByCompanyEmail(String email) {
        Company company = companyRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return company;
    }


}
