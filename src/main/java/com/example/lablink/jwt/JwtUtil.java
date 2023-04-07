package com.example.lablink.jwt;

import com.example.lablink.company.entity.Company;
import com.example.lablink.company.security.CompanyDetailsServiceImpl;
import com.example.lablink.user.security.UserDetailsServiceImpl;

import com.example.lablink.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";     //http요청 응답 보낼 때, 헤더값 key에 해당.
    public static final String AUTHORIZATION_KEY = "role";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    private final UserDetailsServiceImpl userDetailsService;       //스프링 시큐리티
    private final CompanyDetailsServiceImpl companyDetailsService;

    // application.yml 파일에 정의된 JWT secret key
    @Value("${jwt.secret.key}")
    private String secretKey;

    // JWT secret key를 Key 타입으로 변환하여 저장
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // secretKey를 Base64 디코딩하여 Key 타입으로 변환
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기
    // 전달된 HttpServletRequest 객체에서 "Authorization" 헤더의 값에서 토큰을 추출하여 반환
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 유저 토큰 생성
    public String createUserToken(User user) {
        Date date = new Date();

        String token = BEARER_PREFIX +
            Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userRole", null/*, company.getRole()*/)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();

        // token 문자열에서 모든 공백 제거
        return token.replaceAll("\\s+", "");
    }

    // 기업 토큰 생성
    public String createCompanyToken(Company company) {
        Date date = new Date();

        String token = BEARER_PREFIX +
            Jwts.builder()
                .setSubject(company.getEmail())
                .claim("companyRole", null/*, company.getRole()*/)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();

        // token 문자열에서 모든 공백 제거
        return token.replaceAll("\\s+", "");
    }


    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }


    // 토큰에서 사용자 정보 가져오기          //시큐리티
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        UserDetails companyDetails = companyDetailsService.loadUserByUsername(email);

        if (userDetails instanceof User) {
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            return new UsernamePasswordAuthenticationToken(companyDetails, null, companyDetails.getAuthorities());
        }
    }

}
