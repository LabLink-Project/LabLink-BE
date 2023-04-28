package com.example.lablink.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyChatRoomResponseDto {
    private List<MessageListDto> messages;
    private List<RoomListDto> rooms;
}
