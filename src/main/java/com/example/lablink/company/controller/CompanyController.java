package com.example.lablink.company.controller;

import com.example.lablink.company.dto.request.CompanyLoginRequestDto;
import com.example.lablink.company.dto.request.CompanySignupRequestDto;
import com.example.lablink.company.service.CompanyService;
import com.example.lablink.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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
        return ResponseMessage.SuccessResponse(companyService.companyLogin(companyLoginRequestDto, response), "");
    }
}
