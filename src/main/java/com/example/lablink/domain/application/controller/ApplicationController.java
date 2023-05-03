package com.example.lablink.domain.application.controller;

import com.example.lablink.domain.application.dto.Request.ApplicationRequestDto;
import com.example.lablink.domain.application.dto.Request.ApplicationStatusRequestDto;
import com.example.lablink.domain.application.service.ApplicationCompanyService;
import com.example.lablink.domain.application.service.ApplicationService;
import com.example.lablink.domain.company.security.CompanyDetailsImpl;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.global.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "application", description = "application API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/studies/{studyId}")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationCompanyService applicationCompanyService;

    @Operation(summary = "신청서 작성", description = "신청서 작성")
    @PostMapping("/applications")
    public ResponseEntity addApplication(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long studyId, @RequestBody ApplicationRequestDto applicationRequestDto){
        applicationService.addApplication(userDetails, studyId,applicationRequestDto);
        return ResponseMessage.SuccessResponse("신청서 작성 성공","");
    }

    /*@Operation(summary = "신청서 수정", description = "신청서 수정")
    @PutMapping("/{applicationId}")
    public ResponseEntity editApplication(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long studyId,@RequestBody ApplicationRequestDto applicationRequestDto,@PathVariable Long applicationId){
        applicationService.modifyApplication(userDetails,studyId,applicationRequestDto,applicationId);
        return ResponseMessage.SuccessResponse("신청서 수정 성공","");
    }*/

    @Operation(summary = "신청서 삭제", description = "신청서 삭제")
    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity deleteApplication(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long studyId,@PathVariable Long applicationId){
        applicationService.deleteApplication(userDetails,studyId,applicationId);
        return ResponseMessage.SuccessResponse("신청서 삭제 성공","");
    }

    @Operation(summary = "신청서 접수 클릭 시 나오는 정보 값", description = "신청서 접수 클릭 시 나오는 정보 값")
    @PostMapping("/applicationsInfo")
    public ResponseEntity afterApplication(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long studyId){
        return ResponseMessage.SuccessResponse("",applicationService.afterApplication(userDetails,studyId));
    }

    @Operation(summary = "신청서 승인, 거절", description = "신청서 승인, 거절")
    @PatchMapping("/applications/{applicationId}/status")
    public ResponseEntity applicationStatus(@AuthenticationPrincipal CompanyDetailsImpl companyDetails, @RequestBody ApplicationStatusRequestDto statusRequestDto, @PathVariable Long studyId, @PathVariable Long applicationId){
        applicationCompanyService.applicationStatus(companyDetails, statusRequestDto, studyId, applicationId);
        return ResponseMessage.SuccessResponse("완료.", "");
    }

    @Operation(summary = "공고별 전체 신청서 확인", description = "공고별 전체 신청서 확인")
    @GetMapping("/applications")
    public ResponseEntity applicationFromStudy(@AuthenticationPrincipal CompanyDetailsImpl companyDetails, @PathVariable Long studyId) {
        return ResponseMessage.SuccessResponse("조회 성공", applicationCompanyService.applicationFromStudy(companyDetails, studyId));
    }

    @Operation(summary = "기업의 상세 신청서 확인", description = "기업의 상세 신청서 확인")
    @GetMapping("/applications/{applicationId}")
    public ResponseEntity companyDetailApplicationFromStudy(@AuthenticationPrincipal CompanyDetailsImpl companyDetails, @PathVariable Long studyId,@PathVariable Long applicationId){
        return ResponseMessage.SuccessResponse("",applicationCompanyService.companyDetailApplicationFromStudy(companyDetails,studyId,applicationId));
    }
//
//    @Operation(summary = "유저의 상세 신청서 확인", description = "유저의 상세 신청서 확인")
//    @GetMapping("/applications/{applicationId}/user")
//    public ResponseEntity userDetailApplicationFromStudy(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long studyId,@PathVariable Long applicationId){
//        return ResponseMessage.SuccessResponse("",applicationService.userDetailApplicationFromStudy(userDetails, studyId,applicationId));
//    }
}
