package com.example.lablink.user.kakao.controller;

import com.example.lablink.jwt.JwtUtil;
import com.example.lablink.message.ResponseMessage;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.kakao.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class KaKaoUserController {

    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @GetMapping("/kakao/login")
    public ResponseEntity kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        User kakaoUser = kakaoService.kakaoLogin(code, response);
        String createToken =  jwtUtil.createUserToken(kakaoUser);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

        return ResponseMessage.SuccessResponse("로그인 성공!", "");
    }
}
