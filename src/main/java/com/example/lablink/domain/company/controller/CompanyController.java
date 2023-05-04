package com.example.lablink.domain.company.controller;

import com.example.lablink.domain.user.dto.request.SignupRequestDto;
import com.example.lablink.global.S3Image.dto.S3ResponseDto;
import com.example.lablink.global.S3Image.service.S3UploaderService;
import com.example.lablink.domain.company.dto.request.CompanyLoginRequestDto;
import com.example.lablink.domain.company.dto.request.CompanyNameCheckRequestDto;
import com.example.lablink.domain.company.dto.request.CompanySignupRequestDto;
import com.example.lablink.domain.company.security.CompanyDetailsImpl;
import com.example.lablink.domain.company.service.CompanyService;
import com.example.lablink.global.common.dto.request.SignupEmailCheckRequestDto;
import com.example.lablink.global.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "Company", description = "Company API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final S3UploaderService s3UploaderService;

    @Operation(summary = "기업 회원가입", description = "기업 회원가입")
    @PostMapping("/signup")
    public ResponseEntity companySignup(@Valid /*@RequestBody */CompanySignupRequestDto companySignupRequestDto) {
        S3ResponseDto s3ResponseDto = null;
        if(companySignupRequestDto.getLogo() != null){
            MultipartFile image = companySignupRequestDto.getLogo();
            s3ResponseDto = s3UploaderService.uploadFiles("logo", image);
        }
        companyService.companySignup(companySignupRequestDto, s3ResponseDto);
        return ResponseMessage.SuccessResponse("회원가입 완료.", "");
    }

    @Operation(summary = "기업 로그인", description = "기업 로그인")
    @PostMapping("/login")
    public ResponseEntity companyLogin(@RequestBody CompanyLoginRequestDto companyLoginRequestDto, HttpServletResponse response) {
        companyService.companyLogin(companyLoginRequestDto, response);
        return ResponseMessage.SuccessResponse("로그인 완료", "");
    }

    @Operation(summary = "기업 이메일 체크", description = "기업 이메일 체크")
    @PostMapping("/signup/email-check")
    public ResponseEntity emailCheck(@RequestBody @Valid SignupEmailCheckRequestDto signupEmailCheckRequestDto) {
        companyService.emailCheck(signupEmailCheckRequestDto);
        return ResponseMessage.SuccessResponse("사용 가능합니다.", "");
    }

    @Operation(summary = "기업명 체크", description = "기업명 체크")
    @PostMapping("/signup/company-name-check")
    public ResponseEntity companyNameCheck(@RequestBody @Valid CompanyNameCheckRequestDto companyNameCheckRequestDto) {
        companyService.companyNameCheck(companyNameCheckRequestDto);
        return ResponseMessage.SuccessResponse("사용 가능합니다.", "");
    }

    @Operation(summary = "기업 로그아웃", description = "기업 로그아웃")
    //csrf 방지위해 post방식
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
//        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, null);
    }

    @Operation(summary = "기업 회원 탈퇴", description = "기업 회원 탈퇴")
    @DeleteMapping("")
    public ResponseEntity deleteCompany(@AuthenticationPrincipal CompanyDetailsImpl companyDetails, HttpServletResponse response) {
        companyService.deleteCompany(companyDetails, response);
        return ResponseMessage.SuccessResponse("탈퇴 완료.", "");
    }

    @Operation(summary = "내 공고 확인", description = "내 공고 확인")
    @GetMapping("/studies")
    public ResponseEntity viewMyStudies(@AuthenticationPrincipal CompanyDetailsImpl companyDetails) {
        return ResponseMessage.SuccessResponse("조회 성공", companyService.viewMyStudies(companyDetails));
    }
}
