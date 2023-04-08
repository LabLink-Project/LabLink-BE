package com.example.lablink.company.controller;

import com.example.lablink.company.dto.request.CompanyEmailCheckRequestDto;
import com.example.lablink.company.dto.request.CompanyLoginRequestDto;
import com.example.lablink.company.dto.request.CompanyNameCheckRequestDto;
import com.example.lablink.company.dto.request.CompanySignupRequestDto;
import com.example.lablink.company.service.CompanyService;
import com.example.lablink.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // 기업 회원가입
    @PostMapping("/companies/signup")
    public ResponseEntity companySignup(@RequestBody CompanySignupRequestDto companySignupRequestDto) {
        return ResponseMessage.SuccessResponse(companyService.companySignup(companySignupRequestDto), "");
    }

    // 기업 로그인
    @PostMapping("/companies/login")
    public ResponseEntity companyLogin(@RequestBody CompanyLoginRequestDto companyLoginRequestDto, HttpServletResponse response) {
        companyService.companyLogin(companyLoginRequestDto, response);
        return ResponseMessage.SuccessResponse("로그인 완료", "");
    }

    // 기업 이메일 재입력 체크
    @PostMapping("/companies/signup/email-check")
    public ResponseEntity emailCheck(@RequestBody @Valid CompanyEmailCheckRequestDto companyEmailCheckRequestDto) {
        companyService.emailCheck(companyEmailCheckRequestDto);
        return ResponseMessage.SuccessResponse("사용 가능합니다.", "");
    }

    // 기업명 재입력 체크
    @PostMapping("/companies/signup/company-name-check")
    public ResponseEntity companyNameCheck(@RequestBody @Valid CompanyNameCheckRequestDto companyNameCheckRequestDto) {
        companyService.companyNameCheck(companyNameCheckRequestDto);
        return ResponseMessage.SuccessResponse("사용 가능합니다.", "");
    }
}
