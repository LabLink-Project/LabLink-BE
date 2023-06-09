package com.example.lablink.domain.user.controller;

import com.example.lablink.domain.user.dto.request.UserNickNameRequestDto;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.global.common.dto.request.SignupEmailCheckRequestDto;
import com.example.lablink.global.jwt.JwtUtil;
import com.example.lablink.global.message.ResponseMessage;
import com.example.lablink.domain.user.dto.request.LoginRequestDto;
import com.example.lablink.domain.user.dto.request.SignupRequestDto;
import com.example.lablink.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "User", description = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 회원가입", description = "유저 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseMessage.SuccessResponse(userService.signup(signupRequestDto), "");
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return ResponseMessage.SuccessResponse(userService.login(loginRequestDto, response), "");
    }

    @Operation(summary = "유저 닉네임 중복 체크", description = "유저 닉네임 중복 체크")
    @PostMapping("/signup/nickName-check")
    public ResponseEntity<?> nickNameCheck(@RequestBody UserNickNameRequestDto userNickNameRequestDto) {
        return ResponseMessage.SuccessResponse(userService.nickNameCheck(userNickNameRequestDto), "");
    }

    @Operation(summary = "유저 이메일 중복 체크", description = "유저 이메일 중복 체크")
    @PostMapping("/signup/email-check")
    public ResponseEntity<?> emailCheck(@RequestBody @Valid SignupEmailCheckRequestDto signupEmailCheckRequestDto) {
        userService.emailCheck(signupEmailCheckRequestDto);
        return ResponseMessage.SuccessResponse("사용 가능합니다.", "");
    }

    @Operation(summary = "유저 로그아웃", description = "유저 로그아웃")
    //csrf 방지위해 post방식
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
//        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, null);
    }

    @Operation(summary = "유저 회원탈퇴", description = "유저 회원탈퇴")
    @DeleteMapping("")
    public ResponseEntity<?> deleteUser (@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        return ResponseMessage.SuccessResponse(userService.deleteUser(userDetails, response), "");
    }

    @Operation(summary = "내 실험 관리(내 신청서 조회)", description = "내 실험 관리(내 신청서 조회)")
    @GetMapping("/applications")
    public ResponseEntity<?> getMyLabs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("조회 성공", userService.getMyLabs(userDetails));
    }

    @Operation(summary = "refreshToken", description = "refreshToken")
    @GetMapping("issue/token")
    public ResponseEntity<?> refreshAccessToken(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request, HttpServletResponse response) {
        String newAccessToken = userService.refreshAccessToken(userDetails, request, response);
        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);

        ResponseEntity<?> successResponse = ResponseMessage.SuccessResponse("리프레시토큰 발급 완료.", "");
        return ResponseEntity.status(successResponse.getStatusCode())
            .headers(headers)
            .body(successResponse.getBody());
    }

}