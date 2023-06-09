package com.example.lablink.domain.study.controller;

import com.example.lablink.global.S3Image.dto.S3ResponseDto;
import com.example.lablink.global.S3Image.service.S3UploaderService;
import com.example.lablink.domain.company.security.CompanyDetailsImpl;
import com.example.lablink.domain.study.dto.StudySearchOption;
import com.example.lablink.domain.study.dto.requestDto.StudyRequestDto;
import com.example.lablink.domain.study.dto.responseDto.LatestSearchKeyword;
import com.example.lablink.domain.study.service.StudySearchService;
import com.example.lablink.domain.study.service.StudyService;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.global.message.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
        S3ResponseDto thumbnailS3ResponseDto = null;
        S3ResponseDto detailS3ResponseDto = null;
        if(requestDto.getThumbnailImage() != null){
            MultipartFile thumbnailImage = requestDto.getThumbnailImage();
            thumbnailS3ResponseDto = s3UploaderService.uploadFiles("thumbnail", thumbnailImage);
        }
        if(requestDto.getDetailImage() != null){
            MultipartFile detailImage = requestDto.getDetailImage();
            detailS3ResponseDto = s3UploaderService.uploadFiles("detail", detailImage);

        }
        studyService.createStudy(requestDto, companyDetails, thumbnailS3ResponseDto, detailS3ResponseDto);
        return ResponseMessage.SuccessResponse("게시글 작성 성공", "");
    }

    // 게시글 조회
    @Operation(summary = "공고 전체 조회", description = "공고 전체 조회 및 검색, 정렬")
    @GetMapping()
    public ResponseEntity getStudies(
            @ModelAttribute StudySearchOption searchOption,
            @RequestParam(defaultValue = "0") int pageIndex,
            @RequestParam(defaultValue = "10") int pageCount,
            @RequestParam(required = false) String sortedType,
            @AuthenticationPrincipal UserDetailsImpl userDetails, @AuthenticationPrincipal CompanyDetailsImpl companyDetails) {
        // searchOption 객체를 사용하여 검색 조건을 처리합니다.
        // pageIndex와 pageCount 파라미터는 기본값을 설정하여 받습니다.
        return ResponseMessage.SuccessResponse("조회 성공",
                studySearchService.getStudies(searchOption, searchOption.getKeyword(), pageIndex, pageCount, sortedType, userDetails, companyDetails));
    }

    // done : ResponseEntity로
    // done : 최근 검색 기록 삭제 기능 추가
    @GetMapping("/search/rank")
    public ResponseEntity searchRankList(){
        return ResponseMessage.SuccessResponse("인기 검색어 조회 성공", studySearchService.searchRankList());
    }

    @GetMapping("/search/latest")
    public ResponseEntity latestSearchKeyword(@AuthenticationPrincipal UserDetailsImpl userDetails, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        List<LatestSearchKeyword> latestSearchKeywords = new ArrayList<>();
        if(userDetails != null){
            latestSearchKeywords = studySearchService.latestSearchKeyword(userDetails);
        }
        if(companyDetails != null){
            latestSearchKeywords = studySearchService.latestSearchKeywordCompany(companyDetails);
        }
        return ResponseMessage.SuccessResponse("최신 검색어 조회 성공", latestSearchKeywords);
    }

    @DeleteMapping("/search/latest")
    public ResponseEntity deleteSearchKeyword(@AuthenticationPrincipal UserDetailsImpl userDetails, @AuthenticationPrincipal CompanyDetailsImpl companyDetails, @RequestParam String deleteWord){
        if(companyDetails != null){
            studySearchService.deleteSearchKeywordCompany(companyDetails, deleteWord);
        }
        if(userDetails != null){
            studySearchService.deleteSearchKeyword(userDetails, deleteWord);
        }
        return ResponseMessage.SuccessResponse("최신 검색어 삭제 성공", "");
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
    @PatchMapping("/{studyId}")
    public ResponseEntity updateStudy(@PathVariable Long studyId, StudyRequestDto requestDto, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        studyService.updateStudy(studyId, requestDto, companyDetails);
        return ResponseMessage.SuccessResponse("공고 수정 성공", "");
    }

    // 기본 이미지로 변경 (thumbnail)
    // todo : patch로 변경
    @DeleteMapping("/{studyId}/thumbnail")
    public ResponseEntity deleteThumbnail(@PathVariable Long studyId, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        studyService.deleteThumbnail(studyId, companyDetails);
        return ResponseMessage.SuccessResponse("썸네일 삭제 성공", "");
    }


    // 기본 이미지로 변경 (detailImage)
    @DeleteMapping("/{studyId}/detailImage")
    public ResponseEntity deleteDetailImage(@PathVariable Long studyId, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        studyService.deleteDetailImage(studyId, companyDetails);
        return ResponseMessage.SuccessResponse("썸네일 삭제 성공", "");
    }

    // 게시글 삭제
    @Operation(summary = "공고 삭제", description = "공고 삭제")
    @DeleteMapping("/{studyId}")
    public ResponseEntity deleteStudy(@PathVariable Long studyId, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        studyService.deleteStudy(studyId, companyDetails);
        return ResponseMessage.SuccessResponse("공고 삭제 성공", "");
    }

}
