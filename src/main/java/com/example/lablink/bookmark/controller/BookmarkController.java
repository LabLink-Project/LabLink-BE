package com.example.lablink.bookmark.controller;

import com.example.lablink.bookmark.service.BookmarkService;
import com.example.lablink.message.ResponseMessage;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;
    @PostMapping("/studies/{studyId}/bookmark")
    public ResponseEntity bookmark(@PathVariable Long studyId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse(bookmarkService.bookmark(studyId, userDetails.getUser()), "");
    }
}
