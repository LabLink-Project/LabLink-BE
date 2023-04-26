package com.example.lablink.user.google.controller;

import com.example.lablink.message.ResponseMessage;
import com.example.lablink.user.google.service.OauthService;
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

    @GetMapping("/users/google/login")
    public ResponseEntity googleLogin(@RequestParam String code, @RequestParam String scope, HttpServletResponse response) throws JsonProcessingException {
        System.out.println("__________code" + code);
        System.out.println("__________scope" + scope);
        oauthService.googleLogin(code, scope, response);
        return ResponseMessage.SuccessResponse("완료", "");
//        return ResponseMessage.SuccessResponse(oauthService.googleLogin(code, scope, response), "");
//        String createToken = oauthService.googleLogin(code, scope, response);
//
//        // Cookie 생성 및 직접 브라우저에 Set
//        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
//        cookie.setPath("/");
//        response.addCookie(cookie);
////        return ResponseMessage.SuccessResponse(oauthService.googleLogin(code, response, request), "");
////        return "redirect:/auth/google/callback";
//        return ResponseMessage.SuccessResponse("완료", "");
    }

//    @GetMapping("/auth/google/callback")
//    public String googleCallback(@RequestParam("code") String code, HttpServletResponse response, HttpServletRequest request) {
//        try {
//            String jwtToken = oauthService.googleLogin(code, response, request);
//            return "JWT Token: " + jwtToken;
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }

//    @GetMapping("/auth/google/callback")
//    public String googleLoginCallback(@RequestParam String code, HttpServletResponse response, HttpServletRequest request) {
//        String jwtToken;
//        try {
//            jwtToken = oauthService.googleLogin(code, response, request);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error: " + e.getMessage();
//        }
//        return "Your JWT token is: " + jwtToken;
//    }

//    @GetMapping("/auth/google/callback")
//    public String googleLogin(@RequestParam String code, HttpServletResponse response, HttpServletRequest request) throws IOException {
//        String createToken = oauthService.googleLogin(code, response, request);
//
//        // Cookie 생성 및 직접 브라우저에 Set
//        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        return "redirect:/auth/google/callback";
//    }
}
