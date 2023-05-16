package com.example.lablink.domain.user.google.service;

import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.entity.UserRoleEnum;
import com.example.lablink.domain.user.repository.UserRepository;
import com.example.lablink.domain.user.google.dto.GoogleUserInfoDto;
import com.example.lablink.global.exception.GlobalErrorCode;
import com.example.lablink.global.exception.GlobalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@Slf4j
public class OauthService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

//    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
//    private String REDIRECT_URI;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User googleLogin(String code, String scope, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        // 인가코드 -> 로그인 후 서비스제공자(구글)로부터 받는 임시 코드
        // 인가코드는 일회성 그리고 짧은 시간내에 사용되어야함
        String accessToken = getToken(code, scope);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        // 액세스 토큰 서비스 제공자(카카오) api 호출할 떄 사용하는 인증 수단
        // 액세스 토큰으로 추가 정보를 요청할 수 있고 이용자의 동의를 얻은 기능 실행 가능(친구목록, 메시지 전송, 프로필가져오기 등??)
        // 액세스 토큰 만료시 리프레시토큰으로 새로 발급
        GoogleUserInfoDto googleUserInfo = getGoogleUserInfo(accessToken);

        // 3. 필요시에 회원가입
        registerGoogleUserIfNeeded(googleUserInfo);

        // 4. JWT 토큰 반환
//        String createToken =  jwtUtil.createUserToken(googleUser);
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, createToken);
//        return createToken;
        return userRepository.findByGoogleEmail(googleUserInfo.getEmail()).orElseThrow(() -> new GlobalException(GlobalErrorCode.USER_NOT_FOUND));
    }


    // 1. "인가 코드"로 "액세스 토큰" 요청
    private String getToken(String code, String scope) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
//        body.add("client_id", "1037318704574-3hujk7ftivb5rhd49tha2mjpcv71edcs.apps.googleusercontent.com");
//        body.add("client_secret", "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");
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
//        body.add("redirect_uri", "http://localhost:8080/auth/google/callback");
//        body.add("redirect_uri", "http://localhost:8080/users/google/login");
        body.add("redirect_uri", "http://localhost:3000/users/google/login");
        body.add("scope", scope);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
            new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            "https://oauth2.googleapis.com/token",
//            "https://www.googleapis.com/oauth2/v4/token",
//            "https://accounts.google.com/o/oauth2/token",
            HttpMethod.POST,
            googleTokenRequest,
            String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        System.out.println("___________________________responsebody" + responseBody);
        System.out.println("+++++++++++++++++++++++++json node" + jsonNode);
        return jsonNode.get("access_token").asText();
    }

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private GoogleUserInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {
        // Create HTTP Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken); // Add space after "Bearer"
        headers.add("Content-type", "application/x-www-form-urlencoded; charset=utf-8");

        // send HTTP request
        HttpEntity<MultiValueMap<String, String>> googleUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo"; // Set the correct user info endpoint
        ResponseEntity<String> response = rt.exchange(
            userInfoEndpoint,
            HttpMethod.POST,
            googleUserInfoRequest,
            String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("sub").asLong();
        String nickname = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();

        log.info("Google user information: " + id + ", " + nickname + ", " + email);
        return new GoogleUserInfoDto(id, nickname, email);
    }


    // 3. 필요시에 회원가입
    private void registerGoogleUserIfNeeded(GoogleUserInfoDto googleUserInfo) {
        // DB 에 중복된 구글 Id 가 있는지 확인
        Long googleId = googleUserInfo.getId();
        User googleUser = userRepository.findById(googleId)
            .orElse(null);
        if (googleUser == null) {
            // 구글 사용자 email 동일한 email 가진 회원이 있는지 확인
            String googleEmail = googleUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(googleEmail).orElse(null);
            if (sameEmailUser != null) {
                googleUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                googleUser = googleUser.googleIdUpdate(googleId.toString());
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = googleUserInfo.getEmail();

                googleUser = new User(encodedPassword, email, googleUserInfo.getNickname(), UserRoleEnum.USER, googleId.toString());
            }

            userRepository.save(googleUser);
        }
    }


}