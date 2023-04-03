package com.example.lablink.user.service;

import com.example.lablink.jwt.JwtUtil;
import com.example.lablink.user.dto.request.LoginRequestDto;
import com.example.lablink.user.dto.request.SignupRequestDto;
import com.example.lablink.user.dto.request.TermsRequestDto;
import com.example.lablink.user.entity.Terms;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.exception.UserErrorCode;
import com.example.lablink.user.exception.UserException;
import com.example.lablink.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TermsService termsService;

    // 유저 회원가입
    public String signup(SignupRequestDto signupRequestDto, TermsRequestDto termsRequestDto) {
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 이메일 중복확인 후 저장
        Optional<User> userFound = userRepository.findByEmail(email);
        if (userFound.isPresent()) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        // 약관 저장 및 유저에게
        Terms terms = termsService.saveTerms(termsRequestDto);
        userRepository.save(new User(email, password, signupRequestDto, terms));

        return "회원가입 완료.";
    }

    // 유저 로그인
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // 이메일 존재, 일치 여부
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND));

        // 비밀번호 일치 여부
        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getLoginid()));

        return "로그인 완료";
    }
}
