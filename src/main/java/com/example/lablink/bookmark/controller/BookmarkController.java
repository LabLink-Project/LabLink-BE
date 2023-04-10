package com.example.lablink.bookmark.controller;

import com.example.lablink.bookmark.service.BookmarkService;
import com.example.lablink.message.ResponseMessage;
import com.example.lablink.user.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "bookmark", description = "bookmark API")
@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;
    @Operation(summary = "북마크", description = "북마크")
    @PostMapping("/studies/{studyId}/bookmark")
    public ResponseEntity bookmark(@PathVariable Long studyId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse(bookmarkService.bookmark(studyId, userDetails), "");
    }
}
