package com.example.lablink.user.kakao.service;

import com.example.lablink.user.exception.UserErrorCode;
import com.example.lablink.user.exception.UserException;
import com.example.lablink.user.kakao.dto.KakaoUserInfoDto;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.entity.UserRoleEnum;
import com.example.lablink.user.repository.UserRepository;
import com.example.lablink.user.service.TermsService;
import com.example.lablink.user.service.UserInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final UserRepository userRepository;
    private final UserInfoService userInfoService;
    private final TermsService termsService;

    @Transactional
    public User kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 신규 유저  회원가입
        signupIfNeeded(kakaoUserInfo);

        //4. 회원 정보 반환
//        Map<String, String> result = new HashMap<>();
//        result.put("kakaoId", String.valueOf(kakaoUserInfo.getId()));
//        result.put("nickname", kakaoUserInfo.getNickname());

        // todo: 그냥 kakaoUserInfo.getId(), kakaoUserInfo.getNickname() 넘겨서 토큰 만드는 걸로 고치자 나중에
        return userRepository.findByKakaoId(kakaoUserInfo.getId()).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
    private String getToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "413b7a48bac324a9a2db2d78b3136b08");
        body.add("redirect_uri", "http://localhost:3000/users/kakao/login");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();


        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    // 3. 회원가입
    private void signupIfNeeded(KakaoUserInfoDto kakaoUserInfo/*, TermsRequestDto termsRequestDto*/) {
        Long kakaoId = kakaoUserInfo.getId();
        String nickname = kakaoUserInfo.getNickname();
        String email = kakaoUserInfo.getEmail();

        // 이미 회원가입한 사용자인지 확인
        if (userRepository.existsByKakaoId(kakaoId)) {
            return;
        }

        // 약관 동의 추가
        // 필수 약관 동의
        /*if(!termsRequestDto.isAgeCheck() || !termsRequestDto.isTermsOfServiceAgreement() || !termsRequestDto.isPrivacyPolicyConsent() || !termsRequestDto.isSensitiveInfoConsent()) {
            throw new UserException(UserErrorCode.NEED_AGREE_REQUIRE_TERMS);
        }*/

        User user = userRepository.save(new User(kakaoId, nickname, email, UserRoleEnum.USER));
//        termsService.saveSocialTerms(termsRequestDto, user);
    }
}
