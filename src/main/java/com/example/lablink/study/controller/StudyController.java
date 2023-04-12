package com.example.lablink.study.controller;

import com.example.lablink.S3Image.dto.S3ResponseDto;
import com.example.lablink.S3Image.service.S3UploaderService;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.message.ResponseMessage;
import com.example.lablink.study.dto.StudySearchOption;
import com.example.lablink.study.dto.requestDto.StudyRequestDto;
import com.example.lablink.study.dto.responseDto.LatestSearchKeyword;
import com.example.lablink.study.dto.responseDto.SearchRankResponseDto;
import com.example.lablink.study.service.StudySearchService;
import com.example.lablink.study.service.StudyService;
import com.example.lablink.user.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Tag(name = "study", description = "study API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/studies")
public class StudyController {
    private final StudyService studyService;
    private final S3UploaderService s3UploaderService;
    private final StudySearchService studySearchService;

    // 게시글 작성
    @Operation(summary = "공고 작성", description = "공고 작성")
    @PostMapping()
    public ResponseEntity createStudy(StudyRequestDto requestDto
            , @AuthenticationPrincipal CompanyDetailsImpl companyDetails
            /*, @RequestParam(value="image") MultipartFile image */){
        S3ResponseDto s3ResponseDto = null;
        if(requestDto.getImage() != null){
            MultipartFile image = requestDto.getImage();
            s3ResponseDto = s3UploaderService.uploadFiles("thumbnail", image);
        }
        studyService.createStudy(requestDto, companyDetails, s3ResponseDto);
        return ResponseMessage.SuccessResponse("게시글 작성 성공", "");
    }

    // 게시글 조회
    @Operation(summary = "공고 전체 조회", description = "공고 전체 조회 및 검색, 정렬")
    @GetMapping()
    public ResponseEntity getStudies(
            @ModelAttribute StudySearchOption searchOption,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "10") int pageCount,
            @RequestParam(required = false) String sortedType,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // searchOption 객체를 사용하여 검색 조건을 처리합니다.
        // pageIndex와 pageCount 파라미터는 기본값을 설정하여 받습니다.
        return ResponseMessage.SuccessResponse("조회 성공",
                studySearchService.getStudies(searchOption, pageIndex, pageCount, sortedType, userDetails));
    }

    @GetMapping("/search/rank")
    public List<SearchRankResponseDto> searchRankList(){
        return studySearchService.searchRankList();
    }

    @GetMapping("/search/latest")
    public List<LatestSearchKeyword> latestSearchKeyword(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return studySearchService.latestSearchKeyword(userDetails);
    }

    // 게시글 관심 공고 조회
//    @GetMapping("/sorting")
//    public ResponseEntity getSortedStudies(@RequestParam(required = false) String sortedType, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return ResponseMessage.SuccessResponse("조회 성공",
//                studyService.getSortedStudies(sortedType, userDetails));
//    }

    // 게시글 상세 조회
    @Operation(summary = "공고 상세 조회", description = "공고 상세 조회")
    @GetMapping("/{studyId}")
    public ResponseEntity getDetailStudy(@PathVariable Long studyId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("조회 성공",
                studyService.getDetailStudy(studyId, userDetails));
    }

    // 게시글 수정
    @Operation(summary = "공고 수정", description = "공고 수정")
    @PutMapping("/{studyId}")
    public ResponseEntity updateStudy(@PathVariable Long studyId, StudyRequestDto requestDto, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        studyService.updateStudy(studyId, requestDto, companyDetails);
        return ResponseMessage.SuccessResponse("공고 수정 성공", "");
    }

    // 게시글 삭제
    @Operation(summary = "공고 삭제", description = "공고 삭제")
    @DeleteMapping("/{studyId}")
    public ResponseEntity deleteStudy(@PathVariable Long studyId, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        studyService.deleteStudy(studyId, companyDetails);
        return ResponseMessage.SuccessResponse("공고 삭제 성공", "");
    }

}
