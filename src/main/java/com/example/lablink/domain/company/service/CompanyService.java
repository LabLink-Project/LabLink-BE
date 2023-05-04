
package com.example.lablink.domain.company.service;

import com.example.lablink.domain.user.service.UserService;
import com.example.lablink.global.S3Image.dto.S3ResponseDto;
import com.example.lablink.domain.company.dto.request.CompanyLoginRequestDto;
import com.example.lablink.domain.company.dto.request.CompanyNameCheckRequestDto;
import com.example.lablink.domain.company.dto.request.CompanySignupRequestDto;
import com.example.lablink.domain.company.dto.response.ViewMyStudyResponseDto;
import com.example.lablink.domain.company.entity.Company;
import com.example.lablink.domain.company.exception.CompanyErrorCode;
import com.example.lablink.domain.company.exception.CompanyException;
import com.example.lablink.domain.company.repository.CompanyRepository;
import com.example.lablink.domain.company.security.CompanyDetailsImpl;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.user.entity.UserRoleEnum;
import com.example.lablink.global.common.dto.request.SignupEmailCheckRequestDto;
import com.example.lablink.global.jwt.JwtUtil;
import com.example.lablink.domain.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Provider;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final StudyService studyService;
    private final @Lazy @Qualifier("userService") Provider<UserService> userServiceProvider;

//    인증 인가를 담당하는 Service의 보안? 을 위함이기에 단익책임 위반 X
//    private final CsrfTokenRepository csrfTokenRepository;

    // 기업 회원가입
    @Transactional
    public void companySignup(CompanySignupRequestDto companySignupRequestDto, S3ResponseDto s3ResponseDto) {
        String email = companySignupRequestDto.getEmail();
        String password = passwordEncoder.encode(companySignupRequestDto.getPassword());

        // 가입 이메일 중복 확인
        checkEmail(email);

        // 유저와 기업의 이메일 중복 검사
        UserService userService = userServiceProvider.get();
        if(userService.existEmail(email)) {
            throw new CompanyException(CompanyErrorCode.DUPLICATE_EMAIL);
        }

        // 로그인시 작성 회사명 db의 회사명의 일치, 존재 확인
        if (companyRepository.existsByCompanyName(companySignupRequestDto.getCompanyName())) {
            throw new CompanyException(CompanyErrorCode.DUPLICATE_COMPANY_NAME);
        }

        Company company;
        if(s3ResponseDto != null) {
            String logoUrl = s3ResponseDto.getUploadFileUrl();
            company = new Company(password, companySignupRequestDto, logoUrl, UserRoleEnum.BUSINESS);
        } else {
            company = new Company(password, companySignupRequestDto, null, UserRoleEnum.BUSINESS);
        }

        companyRepository.save(company);
    }

    // 기업 로그인
    @Transactional
    public void companyLogin(CompanyLoginRequestDto companyLoginRequestDto, HttpServletResponse response) {
        String email = companyLoginRequestDto.getEmail();
        String password = companyLoginRequestDto.getPassword();

        // 로그인시 작성 email과 db의 email의 일치, 존재 확인
        Company company = companyRepository.findByEmail(email).orElseThrow(() -> new CompanyException(CompanyErrorCode.EMAIL_NOT_FOUND));

        // 비밀번호 일치 여부
        if (!passwordEncoder.matches(password, company.getPassword())) {
            throw new CompanyException(CompanyErrorCode.PASSWORD_MISMATCH);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createCompanyToken(company));

        // CSRF, JWT토큰 생성
//        CsrfToken companyCsrfToken = csrfTokenRepository.generateToken(request);
//        String companyToken = jwtUtil.createCompanyToken(company);

        // 쿠키 생성 및 JWT토큰 추가
//        Cookie cookie = new Cookie("Authorization", companyToken);
//        cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 (1일)x
//        cookie.setPath("/"); // 전제api가 쿠키에 액세스 가능
//        cookie.setHttpOnly(true); // XSS공격 방지 (악성코드?)
////        cookie.setSecure(true); // HTTPS 사용 시 설정 (쿠키가 보안되지 않은 연결을 통해 전송되는 경우 탈취 방지)
//        response.addCookie(cookie);

        // 쿠키 생성 및 CSRF토큰 추가
//        Cookie csrfCookie = new Cookie("XSRF-TOKEN", companyCsrfToken.getToken());
//        csrfCookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 (1일)
//        csrfCookie.setPath("/"); // 전제api가 쿠키에 액세스 가능
//        csrfCookie.setHttpOnly(true); // XSS공격 방지 (악성코드?)
////        csrfCookie.setSecure(true); // HTTPS 사용 시 설정 (쿠키가 보안되지 않은 연결을 통해 전송되는 경우 탈취 방지)
//        response.addCookie(csrfCookie);

//        // 세션 쿠키 생성 및 추가 > websecurity 수정 필요
//        HttpSession session = request.getSession(true);
//        session.setAttribute("Authorization", token);
    }

    // 기업 이메일 중복 체크
    @Transactional(readOnly = true)
    public void emailCheck(SignupEmailCheckRequestDto signupEmailCheckRequestDto) {
        if(companyRepository.existsByEmail(signupEmailCheckRequestDto.getEmail())) {
            throw new CompanyException(CompanyErrorCode.DUPLICATE_EMAIL);
        }

        UserService userService = userServiceProvider.get();
        if(userService.existEmail(signupEmailCheckRequestDto.getEmail())) {
            throw new CompanyException(CompanyErrorCode.DUPLICATE_EMAIL);
        }
    }

    // 기업명 중복 체크
    @Transactional
    public void companyNameCheck(CompanyNameCheckRequestDto companyNameCheckRequestDto) {
        if(companyRepository.existsByCompanyName(companyNameCheckRequestDto.getCompanyName())) {
            throw new CompanyException(CompanyErrorCode.DUPLICATE_COMPANY_NAME);
        }
    }

    // 기업 회원 탈퇴
    @Transactional
    public void deleteCompany(CompanyDetailsImpl companyDetails, HttpServletResponse response) {
        List<Study> studies = studyService.findAllCompanyStudy( companyDetails.getCompany());
        for (Study study1 : studies) {
            studyService.deleteStudy(study1.getId(), companyDetails);
        }
        // 로그아웃 (헤더 null값 만들기)
        companyRepository.delete(companyDetails.getCompany());
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, null);
    }

    // 내 공고 확인
    public List<ViewMyStudyResponseDto> viewMyStudies(CompanyDetailsImpl companyDetails) {
//        study.getId()에 해당하는 특정 공고에 대한 값 -> 사용 x
//        studyService.findStudyFromCompany(study.getId(), companyDetails.getCompany());

        // 기업 전체 공고 찾아 리스트에 넣기
        List<Study> studies = studyService.findAllCompanyStudy(companyDetails.getCompany());
        List<ViewMyStudyResponseDto> views = new ArrayList<>();

        for (Study study : studies) {
            views.add(new ViewMyStudyResponseDto(study));
        }

        return views;
    }

    public void checkEmail(String email) {
        if (companyRepository.existsByEmail(email)) {
            throw new CompanyException(CompanyErrorCode.DUPLICATE_EMAIL);
        }
    }

    public boolean existEmail(String email) {
        return companyRepository.existsByEmail(email);
    }
}

