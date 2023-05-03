package com.example.lablink.domain.chat.controller;

import com.example.lablink.domain.chat.dto.ChatMessageDto;
import com.example.lablink.domain.chat.service.ChatService;
import com.example.lablink.domain.company.security.CompanyDetailsImpl;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.global.message.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // user가 방에 입장
    @PostMapping("/chat/room/{studyId}/{nickname}")
    public ResponseEntity enterRoom(@PathVariable Long studyId, @PathVariable String nickname) {
        return ResponseMessage.SuccessResponse("방 입장", chatService.enterRoom(studyId, nickname));
    }

    // user
    @GetMapping("/chat/users/room")
    public ResponseEntity findMessageHistory(@RequestParam(value = "roomId", required = false) String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseMessage.SuccessResponse("유저 대화 불러오기 성공", chatService.findUserMessageHistory(roomId, userDetails.getUser()));
    }

    // company todo : 채팅방 상대가 지원자인지 아닌지 표시해주기
    @GetMapping("/chat/company/room")
    public ResponseEntity findMessageHistory(@RequestParam(value = "roomId", required = false) String roomId, @AuthenticationPrincipal CompanyDetailsImpl companyDetails) {
        return ResponseMessage.SuccessResponse("기업 대화 불러오기 성공", chatService.findCompanyMessageHistory(roomId, companyDetails.getCompany()));
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message) {
        chatService.saveMessage(message);
    }

    @DeleteMapping("/chat/room/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable String roomId) {
        chatService.deleteRoom(roomId);
        return ResponseMessage.SuccessResponse("채팅방 삭제 성공", "");
    }
}
