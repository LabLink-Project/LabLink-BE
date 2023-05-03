package com.example.lablink.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private String roomId; // 방번호
    private String sender; // nickname
    private String content; // 메시지
    private String createdAt;
}
