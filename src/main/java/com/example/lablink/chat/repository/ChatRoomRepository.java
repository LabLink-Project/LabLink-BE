package com.example.lablink.chat.repository;

import com.example.lablink.chat.entity.ChatRoom;
import com.example.lablink.company.entity.Company;
import com.example.lablink.study.entity.Study;
import com.example.lablink.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(String roomId);

    Optional<ChatRoom> findChatRoomByStudyAndUser(Study study, User user);


    @Query(value = "select r from ChatRoom r where r.user = :user")
    List<ChatRoom> findAllChatRoomByUser(@Param("user") User user);

    @Query(value = "select r from ChatRoom r where r.owner = :company")
    List<ChatRoom> findAllChatRoomByCompany(@Param("company") Company company);
    @Modifying
    @Query("DELETE FROM ChatRoom cr WHERE cr.study.id = :studyId")
    void deleteByStudyId(@Param("studyId") Long studyId);
}
