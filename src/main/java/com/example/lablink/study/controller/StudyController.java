package com.example.lablink.study.controller;

import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.message.ResponseMessage;
import com.example.lablink.study.dto.StudySearchOption;
import com.example.lablink.study.dto.requestDto.StudyRequestDto;
import com.example.lablink.study.service.StudyService;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/studies")
public class StudyController {
    private final StudyService studyService;

    // 게시글 작성
    @PostMapping()
    public ResponseEntity createStudy(@RequestBody StudyRequestDto requestDto, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        studyService.createStudy(requestDto, companyDetails);
        return ResponseMessage.SuccessResponse("게시글 작성 성공", "");
    }

    // 게시글 조회
    @GetMapping()
    public ResponseEntity getStudies(
            @ModelAttribute StudySearchOption searchOption,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "10") int pageCount,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // searchOption 객체를 사용하여 검색 조건을 처리합니다.
        // pageIndex와 pageCount 파라미터는 기본값을 설정하여 받습니다.
        return ResponseMessage.SuccessResponse("조회 성공",
                studyService.getStudies(searchOption, pageIndex, pageCount, userDetails));
    }

    // 게시글 관심 공고 조회
    @GetMapping("/sorting")
    public ResponseEntity getSortedStudies(@RequestParam(required = false) String sortedType, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("조회 성공",
                studyService.getSortedStudies(sortedType, userDetails));
    }

    // 게시글 상세 조회
    @GetMapping("/{studyId}")
    public ResponseEntity getDetailStudy(@PathVariable Long studyId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("조회 성공",
                studyService.getDetailStudy(studyId, userDetails));
    }

    // 게시글 수정
    @PutMapping("/{studyId}")
    public ResponseEntity updateStudy(@PathVariable Long studyId, @RequestBody StudyRequestDto requestDto, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        studyService.updateStudy(studyId, requestDto, companyDetails);
        return ResponseMessage.SuccessResponse("공고 수정 성공", "");
    }

    // 게시글 삭제
    @DeleteMapping("/{studyId}")
    public ResponseEntity deleteStudy(@PathVariable Long studyId, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        studyService.deleteStudy(studyId, companyDetails);
        return ResponseMessage.SuccessResponse("공고 삭제 성공", "");
    }

}
