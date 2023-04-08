package com.example.lablink.application.controller;

import com.example.lablink.application.dto.Request.ApplicationRequestDto;
import com.example.lablink.application.service.ApplicationService;
import com.example.lablink.message.ResponseMessage;
import com.example.lablink.user.security.UserDetailsImpl;
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
@RequestMapping("/studies/{studyId}/application")
public class ApplicationController {

    private final ApplicationService applicationService;


    @Operation(summary = "신청서 작성", description = "신청서 작성")
    @PostMapping("/{applicationId}")
    public ResponseEntity addApplication(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long studyId, @RequestBody ApplicationRequestDto applicationRequestDto){
        return ResponseMessage.SuccessResponse(applicationService.addApplication(userDetails, studyId,applicationRequestDto),"");
    }

    @Operation(summary = "신청서 수정", description = "신청서 수정")
    @PutMapping("/{applicationId}")
    public ResponseEntity editApplication(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long studyId,@RequestBody ApplicationRequestDto applicationRequestDto,@PathVariable Long applicationId){
        return ResponseMessage.SuccessResponse(applicationService.modifyApplication(userDetails,studyId,applicationRequestDto,applicationId),"");
    }

    @Operation(summary = "신청서 삭제", description = "신청서 삭제")
    @DeleteMapping("/{applicationId}")
    public ResponseEntity deleteApplication(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long studyId,@PathVariable Long applicationId){
        return ResponseMessage.SuccessResponse(applicationService.deleteApplication(userDetails,studyId,applicationId),"");
    }

    @Operation(summary = "신청서 조회", description = "신청서 조회")
    @GetMapping("/{applicationId}")
    public ResponseEntity getApplication(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long studyId,@PathVariable Long applicationId){
        return ResponseMessage.SuccessResponse("",applicationService.getApplication(userDetails,studyId,applicationId));
    }

    @Operation(summary = "신청서 접수 클릭 시 나오는 정보 값", description = "신청서 접수 클릭 시 나오는 정보 값")
    @PostMapping()
    public ResponseEntity afterApplication(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long studyId){
        return ResponseMessage.SuccessResponse("",applicationService.afterApplication(userDetails,studyId));
    }

}
