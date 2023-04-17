package com.example.lablink.chat.repository;


import com.example.lablink.chat.entity.ChatMessage;
import com.example.lablink.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
//    Page<ChatMessage> findByChatRoom(Pageable pageable, ChatRoom chatRoom);
    List<ChatMessage> findByRoomId(Long roomId);
}
