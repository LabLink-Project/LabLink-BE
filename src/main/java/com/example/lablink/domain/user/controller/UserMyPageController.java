package com.example.lablink.domain.user.controller;

import com.example.lablink.global.message.ResponseMessage;
import com.example.lablink.domain.user.dto.request.MyPageCheckRequestDto;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.domain.user.service.UserMyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "UserMyPage", description = "UserMyPage API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserMyPageController {

    private final UserMyPageService userMyPageService;

    @Operation(summary = "유저 비밀번호 확인", description = "유저 비밀번호 확인")
    @PostMapping("/check")
    public ResponseEntity checkUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MyPageCheckRequestDto checkRequestDto) {
        return ResponseMessage.SuccessResponse("확인 완료", userMyPageService.checkUser(userDetails, checkRequestDto));
    }

    @Operation(summary = "유저 정보 수정", description = "유저 정보 수정")
    @PatchMapping("/check/modifyProfile")
    public ResponseEntity modifyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody MyPageCheckRequestDto.UserModifyRequestDto checkRequestDto) {
        userMyPageService.modifyProfile(userDetails, checkRequestDto);
        return ResponseMessage.SuccessResponse("수정 완료", "");
    }

    @Operation(summary = "유저 비밀번호 변경", description = "유저 비밀번호 변경")
    @PatchMapping("/check/changePassword")
    public ResponseEntity changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody MyPageCheckRequestDto checkRequestDto) {
        userMyPageService.changePassword(userDetails, checkRequestDto);
        return ResponseMessage.SuccessResponse("비밀번호 변경 완료", "");
    }

}
