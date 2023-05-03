package com.example.lablink.domain.user.google.controller;

import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.google.service.OauthService;
import com.example.lablink.global.jwt.JwtUtil;
import com.example.lablink.global.message.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "Google", description = "Google API")
@RestController
@Slf4j
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;
    private final JwtUtil jwtUtil;

    @GetMapping("/users/google/login")
    public ResponseEntity googleLogin(@RequestParam String code, @RequestParam String scope, HttpServletResponse response) throws JsonProcessingException {
        System.out.println("__________code" + code);
        System.out.println("__________scope" + scope);
//        oauthService.googleLogin(code, scope, response);
//        return ResponseMessage.SuccessResponse("로그인 성공", "");

        User googleUser = oauthService.googleLogin(code, scope, response);
        String createToken =  jwtUtil.createUserToken(googleUser);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);

        return ResponseMessage.SuccessResponse("로그인 성공!", "");
    }

}
