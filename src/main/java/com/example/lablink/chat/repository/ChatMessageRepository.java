package com.example.lablink.chat.repository;

import com.example.lablink.chat.entity.ChatMessage;
import com.example.lablink.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoom(ChatRoom room);

    @Query(value = "select r.room_id from chat_room r left join chat_message m on r.id = m.room_id where r.user_id = :userId order by m.id desc;", nativeQuery = true)
    List<String> findRoomIdOfUsersLastChatMessage(@Param("userId") Long userId);

    @Query(value = "select r.room_id from chat_room r left join chat_message m on r.id = m.room_id where r.owner_id = :companyId order by m.id desc;", nativeQuery = true)
    List<String> findRoomIdOfCompanyLastChatMessage(@Param("companyId") Long companyId);

    @Modifying
    @Query("DELETE FROM ChatMessage cm WHERE cm.room.id IN (SELECT cr.id FROM ChatRoom cr WHERE cr.study.id = :studyId)")
    void deleteMessagesByStudyId(@Param("studyId") Long studyId);

    @Query("select m from ChatMessage m where m.room = :room order by m.id desc")
    List<ChatMessage> findLastMessageByRoom(@Param("room") ChatRoom room);

    boolean existsByRoom(ChatRoom room);
    void deleteByRoom(ChatRoom room);
}
