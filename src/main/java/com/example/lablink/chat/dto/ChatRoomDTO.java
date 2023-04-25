package com.example.lablink.chat.dto;

import com.example.lablink.chat.entity.ChatRoom;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ChatRoomDTO {

    private Long roomId;
    private Long userId;
    private Long companyId;
//    private Set<WebSocketSession> sessions = new HashSet<>();
    //WebSocketSession은 Spring에서 Websocket Connection이 맺어진 세션

    public ChatRoomDTO(ChatRoom chatRoom){
        this.roomId = chatRoom.getId();
        this.userId = chatRoom.getUserId();
        this.companyId = chatRoom.getCompanyId();
    }

//    public static ChatRoomDTO createChatRoomDTO(String name){
//        ChatRoomDTO room = new ChatRoomDTO();
//
//        room.roomId = UUID.randomUUID().toString();
//        room.name = name;
//        return room;
//    }
}