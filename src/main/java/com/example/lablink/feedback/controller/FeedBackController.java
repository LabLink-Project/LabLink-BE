package com.example.lablink.feedback.controller;

import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.feedback.dto.Request.FeedBackRequestDto;
import com.example.lablink.feedback.service.FeedBackService;
import com.example.lablink.message.ResponseMessage;
import com.example.lablink.user.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "FeedBack", description = "FeedBack API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/studies/{studyId}/feedback")
public class FeedBackController {

    private final FeedBackService feedBackService;

    @Operation(summary = "피드백 작성", description = "피드백 작성")
    @PostMapping()
    public ResponseEntity addFeedBack(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long studyId, @RequestBody FeedBackRequestDto feedBackRequestDto){
        feedBackService.addFeedBack(userDetails,studyId,feedBackRequestDto);
        return ResponseMessage.SuccessResponse("피드백 작성 성공","");
    }

    @Operation(summary = "피드백 조회", description = "피드백 조회")
    @GetMapping()
    public ResponseEntity getFeedBack(@AuthenticationPrincipal CompanyDetailsImpl companyDetails, @PathVariable Long studyId){

        return ResponseMessage.SuccessResponse("",feedBackService.getFeedBack(companyDetails,studyId));
    }

    @Operation(summary = "피드백 상세조회", description = "피드백 상세조회")
    @GetMapping("/{feedBackId}")
    public ResponseEntity detailFeedBack(@AuthenticationPrincipal CompanyDetailsImpl companyDetails, @PathVariable Long studyId,@PathVariable Long feedbackId){
        return ResponseMessage.SuccessResponse("",feedBackService.getDetailFeedBack(companyDetails,studyId,feedbackId));
    }
    @Operation(summary = "피드백 리스트 다운로드", description = "피드백 리스트 다운로드")
    @GetMapping("/excel/download")
    public ResponseEntity excelDownloadFeedBack(@AuthenticationPrincipal CompanyDetailsImpl companyDetails, @PathVariable Long studyId) throws IOException {
        feedBackService.excelDownloadFeedBack(companyDetails,studyId);
        return ResponseMessage.SuccessResponse("","");
    }
}
