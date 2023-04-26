package com.example.lablink.bookmark.controller;

import com.example.lablink.bookmark.service.BookmarkService;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.message.ResponseMessage;
import com.example.lablink.user.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "bookmark", description = "bookmark API")
@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    // done : 내가 찜한 목록(online, offline)
    // todo : 북마크 조회한 화면에서 북마크 취소 ? - 구현이 되어있는 듯 하다 .. ? (FE이랑 테스트해보기)
    // 유저가 북마크 조회
    @GetMapping("/bookmark")
    public ResponseEntity getUserBookmark(@RequestParam String category, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse("북마크 조회 성공", bookmarkService.getUserBookmark(category, userDetails));
    }

    @Operation(summary = "북마크", description = "북마크")
    @PostMapping("/studies/{studyId}/bookmark")
    public ResponseEntity bookmark(@PathVariable Long studyId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse(bookmarkService.bookmark(studyId, userDetails), "");
    }
}
