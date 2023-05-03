package com.example.lablink.domain.bookmark.controller;

import com.example.lablink.domain.bookmark.dto.BookmarkResponseDto;
import com.example.lablink.domain.bookmark.service.BookmarkService;
import com.example.lablink.domain.company.security.CompanyDetailsImpl;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.global.message.ResponseMessage;
import com.example.lablink.domain.study.exception.StudyErrorCode;
import com.example.lablink.domain.study.exception.StudyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "bookmark", description = "bookmark API")
@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    // done : 내가 찜한 목록(online, offline)
    // done : 북마크 조회한 화면에서 북마크 취소 ? - 구현이 되어있는 듯 하다 .. ? (FE이랑 테스트해보기)
    // 유저가 북마크 조회
    @GetMapping("/bookmark")
    public ResponseEntity getUserBookmark(@AuthenticationPrincipal UserDetailsImpl userDetails, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        List<BookmarkResponseDto> bookmarkResponseDtos = new ArrayList<>();
        if (userDetails != null){
            bookmarkResponseDtos= bookmarkService.getUserBookmark(userDetails);
        }
        if (companyDetails != null){
            bookmarkResponseDtos = bookmarkService.getCompanyBookmark(companyDetails);
        }
        if (userDetails == null && companyDetails == null){
            throw new StudyException(StudyErrorCode.LOGIN_REQUIRED);
        }
        return ResponseMessage.SuccessResponse("북마크 조회 성공", bookmarkResponseDtos);
    }

    @Operation(summary = "북마크", description = "북마크")
    @PostMapping("/studies/{studyId}/bookmark")
    public ResponseEntity bookmark(@PathVariable Long studyId, @AuthenticationPrincipal UserDetailsImpl userDetails, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){
        String message = "";
        if (userDetails != null){
            message = bookmarkService.bookmark(studyId, userDetails);
        }
        if (companyDetails != null){
            message = bookmarkService.bookmark(studyId, companyDetails);
        }
        if (userDetails == null && companyDetails == null){
            throw new StudyException(StudyErrorCode.LOGIN_REQUIRED);
        }
        return ResponseMessage.SuccessResponse(message, "");
    }
}
