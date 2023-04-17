package com.example.lablink.chat.service;

import com.example.lablink.chat.dto.ChatRoomDTO;
import com.example.lablink.chat.entity.ChatRoom;
import com.example.lablink.chat.repository.ChatRoomRepository;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.study.exception.StudyErrorCode;
import com.example.lablink.study.exception.StudyException;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    // todo: 채팅기능 전체 인증인가 추가
    // todo : companyid에 따라 해당 company만의 채팅 내역 보여주기
    @Transactional
    public List<ChatRoomDTO> getAllChatRooms(long companyId, CompanyDetailsImpl companyDetails) {
        List<ChatRoomDTO> chatRoomDTOS = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomRepository.findAllByOrderByCreatedAtDesc()) {
            chatRoomDTOS.add(new ChatRoomDTO(chatRoom));
        }
        return chatRoomDTOS;
    }

    @Transactional
    public long createChatRoom(long companyId, UserDetailsImpl userDetails) {
//        ChatRoom chatRoom = new ChatRoom(userDetails.getUser().getId(), companyId);
        ChatRoom chatRoom = new ChatRoom(1L, companyId);
        chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
    }

    @Transactional
    public ChatRoomDTO getChatRoomDTO(long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                ()-> new StudyException(StudyErrorCode.STUDY_NOT_FOUND)
        );
        return new ChatRoomDTO(chatRoom);
    }

    @Transactional
    public ChatRoom getChatRoom(long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(
                ()-> new StudyException(StudyErrorCode.STUDY_NOT_FOUND)
        );
    }
}
