package com.example.lablink.chat.controller;

import com.example.lablink.chat.dto.ChatMessageDTO;
import com.example.lablink.chat.dto.ChatRoomDTO;
import com.example.lablink.chat.entity.ChatMessage;
import com.example.lablink.chat.service.ChatService;
import com.example.lablink.company.security.CompanyDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate template; // 특정 Broker로 메세지를 전달
    private final ChatService chatService;

    // Client가 SEND할 수 있는 경로
    // stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    // "/pub/chat/enter"
    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDTO message){
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        // /sub/chat/room/{roomId} 경로를 구독하고 있는 클라이언트에게 전달
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message){
        // 채팅 내용 저장 로직 추가
        long roomId = message.getRoomId();
        String content = message.getMessage();
        long writer = message.getWriter();
        // 채팅 내용을 저장하는 로직을 추가하여 데이터베이스나 다른 저장소에 저장하도록 수정
        chatService.saveChatContent(roomId, content, writer);

        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}