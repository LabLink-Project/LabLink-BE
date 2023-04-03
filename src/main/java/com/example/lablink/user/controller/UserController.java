package com.example.lablink.user.controller;

import com.example.lablink.message.ResponseMessage;
import com.example.lablink.user.dto.request.LoginRequestDto;
import com.example.lablink.user.dto.request.SignupRequestDto;
import com.example.lablink.user.dto.request.TermsRequestDto;
import com.example.lablink.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 회원가입
    @PostMapping("/users/signup")
    public ResponseEntity signup(@RequestBody SignupRequestDto signupRequestDto, TermsRequestDto termsRequestDto) {
        return ResponseMessage.SuccessResponse(userService.signup(signupRequestDto, termsRequestDto), null);
    }

    // 유저 로그인
    @PostMapping("/users/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return ResponseMessage.SuccessResponse(userService.login(loginRequestDto, response), null);
    }
}
