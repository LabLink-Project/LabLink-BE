package com.example.lablink.user.controller;

import com.example.lablink.message.ResponseMessage;
import com.example.lablink.user.dto.request.LoginRequestDto;
import com.example.lablink.user.dto.request.SignupRequestDto;
import com.example.lablink.user.dto.request.UserEmailCheckRequestDto;
import com.example.lablink.user.security.UserDetailsImpl;
import com.example.lablink.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    // 유저 회원가입
    @PostMapping("/users/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseMessage.SuccessResponse(userService.signup(signupRequestDto), "");
    }

    // 유저 로그인
    @PostMapping("/users/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return ResponseMessage.SuccessResponse(userService.login(loginRequestDto, response), "");
    }

    // 유저 이메일 중복 체크
    @PostMapping("/users/signup/email-check")
    public ResponseEntity emailCheck(@RequestBody @Valid UserEmailCheckRequestDto userEmailCheckRequestDto) {
        return ResponseMessage.SuccessResponse(userService.emailCheck(userEmailCheckRequestDto), "");
    }

    // 유저 로그아웃
    //csrf 방지위해 post방식
    @PostMapping("/users/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
    }

    // 유저 회원탈퇴
    @PostMapping("/users/mypages/delete")
    public ResponseEntity deleteUser (@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        return ResponseMessage.SuccessResponse(userService.deleteUser(userDetails, response), "");
    }
}
