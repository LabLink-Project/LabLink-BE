package com.example.lablink.jwt;



import com.example.lablink.message.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    /* 요청이 들어올 때마다 실행.
     * 토큰 확인, 토큰 유효성 검사, 토큰에 포함된 정보를 기반으로 인증 수행*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // HTTP 요청에서 Authorization헤더를 찾아 토큰 반환
        String token = jwtUtil.resolveToken(request);
        // 토큰이 있다면 진행
        if(token != null) {
            // 토큰 유효성 검사 -> 만료,위조 등 유효하지 않음
            if(!jwtUtil.validateToken(token)){
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            // 토큰 유효 -> getUserInfoFromToken메서드를 사용해 JWT 토큰의 payload에서 정보 반환
            Claims info = jwtUtil.getUserInfoFromToken(token);    //토큰에서 user정보 가져옴(payload)
            // Claims 객체에서 사용자 이름을 가져와 인증 설정
            setAuthentication(info.getSubject());   //getSubject 헤더값
        }
        // 다음 단계 실행 -> 다른 필터 및 컨트롤러 실행
        filterChain.doFilter(request,response);

        // 세션 쿠키 방식일 때?? 테스트 안해봄
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            String token = (String) session.getAttribute("Authorization");
//            if (token != null) {
//                if (!jwtUtil.validateToken(token)) {
//                    jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
//                    return;
//                }
//                Claims info = jwtUtil.getUserInfoFromToken(token);
//                setAuthentication(info.getSubject());
//            }
//        }
//        filterChain.doFilter(request,response);
    }

    /* 주어진 파라미터 값으로 SecurityContext에 인증 설정.
     * JWT 유틸리티를 사용하여 인증 객체를 생성하고, SecurityContext에 설정*/
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // JWT 인증과 관련된 예외 처리
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");

        try {
            String json = new ObjectMapper().writeValueAsString(ResponseMessage.SuccessResponse(msg, "") );
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
