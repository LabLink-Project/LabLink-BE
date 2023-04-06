package com.example.lablink.bookmark.controller;

import com.example.lablink.bookmark.service.BookmarkService;
import com.example.lablink.message.ResponseMessage;
import com.example.lablink.security.UserDetailsImpl;
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
    @PostMapping("/studies/bookmark/{id}")
    public ResponseEntity bookmark(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseMessage.SuccessResponse(bookmarkService.bookmark(id, userDetails.getUser()), "");
    }
}
