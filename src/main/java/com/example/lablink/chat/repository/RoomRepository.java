package com.example.lablink.chat.repository;

import com.example.lablink.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<ChatRoom, Long> {
//    Optional<ChatRoom> findBySenderAndReceiver(User sender, User receiver);
//    Page<ChatRoom> findAllBySenderOrReceiver(Pageable pageable, User sender, User receiver);
}
