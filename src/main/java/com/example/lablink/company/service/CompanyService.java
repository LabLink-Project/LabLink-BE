package com.example.lablink.company.service;

import com.example.lablink.company.dto.request.CompanyLoginRequestDto;
import com.example.lablink.company.dto.request.CompanySignupRequestDto;
import com.example.lablink.company.entity.Company;
import com.example.lablink.company.exception.CompanyErrorCode;
import com.example.lablink.company.exception.CompanyException;
import com.example.lablink.company.repository.CompanyRepository;
import com.example.lablink.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 기업 회원가입
    public String companySignup(CompanySignupRequestDto companySignupRequestDto) {
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
    public String companyLogin(CompanyLoginRequestDto companyLoginRequestDto, HttpServletResponse response) {
        String email = companyLoginRequestDto.getEmail();
        String password = passwordEncoder.encode(companyLoginRequestDto.getPassword());

        // 로그인시 작성 email과 db의 email의 일치, 존재 확인
        Company company = companyRepository.findByEmail(email).orElseThrow(() -> new CompanyException(CompanyErrorCode.EMAIL_NOT_FOUND));

        if (!passwordEncoder.matches(password, company.getPassword())) {
            throw new CompanyException(CompanyErrorCode.PASSWORD_MISMATCH);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(company.getEmail()));
        return "로그인 완료";
    }

}
