package com.example.lablink.chat.dto;

import com.example.lablink.chat.entity.ChatMessage;
import lombok.*;

@Getter
@Setter
public class ChatMessageDTO {
    private long roomId;
    private long writer;
    private String message;

    // 기본 생성자
    // Spring MVC에서는 JSON 데이터를 자바 객체로 변환할 때, 기본 생성자 필요함
    public ChatMessageDTO() {
    }

    public ChatMessageDTO(ChatMessage chatMessage){
        this.roomId = chatMessage.getRoomId();
        this.writer = chatMessage.getWriter();
        this.message = chatMessage.getMessage();
    }
}