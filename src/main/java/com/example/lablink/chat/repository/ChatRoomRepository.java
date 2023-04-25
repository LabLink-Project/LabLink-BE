package com.example.lablink.chat.repository;

import com.example.lablink.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
    List<ChatRoom> findAllByOrderByCreatedAtDesc();
}