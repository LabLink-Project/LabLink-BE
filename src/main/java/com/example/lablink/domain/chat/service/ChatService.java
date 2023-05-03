package com.example.lablink.domain.chat.service;

import com.example.lablink.domain.chat.dto.*;
import com.example.lablink.domain.chat.entity.ChatMessage;
import com.example.lablink.domain.chat.entity.ChatRoom;
import com.example.lablink.domain.chat.exception.ChatErrorCode;
import com.example.lablink.domain.chat.exception.ChatException;
import com.example.lablink.domain.chat.repository.ChatMessageRepository;
import com.example.lablink.domain.chat.repository.ChatRoomRepository;
import com.example.lablink.domain.company.entity.Company;
import com.example.lablink.domain.study.entity.Study;
import com.example.lablink.domain.study.service.GetStudyService;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final GetStudyService getStudyService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessageSendingOperations template;
    private final UserService userService;
    @Transactional
    public String enterRoom(Long studyId, String nickname) {
        User user = userService.getUserByNickname(nickname);
        Study study = getStudyService.getStudy(studyId);
        Company owner = study.getCompany();
        ChatRoom room = chatRoomRepository.findChatRoomByStudyAndUser(study, user).orElse(null);
        if (room == null) {
            room = new ChatRoom(study, user, owner);
            chatRoomRepository.saveAndFlush(room);
        }
        return room.getRoomId();
    }

    // user
    @Transactional
    public MyChatRoomResponseDto findUserMessageHistory(String roomId, User user) {
        if (roomId == null) {
            List<String> rooms = chatMessageRepository.findRoomIdOfUsersLastChatMessage(user.getId());
            if (rooms.size() > 0) {
                roomId = rooms.get(0);
            }
        }
        return roomId == null ? null : findUserMessageHistoryByRoomId(roomId, user);
    }

    // company
    @Transactional
    public MyChatRoomResponseDto findCompanyMessageHistory(String roomId, Company company) {
        if (roomId == null) {
            List<String> rooms = chatMessageRepository.findRoomIdOfCompanyLastChatMessage(company.getId());
            if (rooms.size() > 0) {
                roomId = rooms.get(0);
            }
        }
        return roomId == null ? null : findCompanyMessageHistoryByRoomId(roomId, company);
    }

    // user
    @Transactional
    public void saveMessage(ChatMessageDto message) {
        User user = userService.getUserByNickname(message.getSender());
        ChatRoom room = chatRoomRepository.findByRoomId(message.getRoomId()).orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND));
        ChatMessage chatMessage = new ChatMessage(user, message.getContent(), room);
        chatMessageRepository.saveAndFlush(chatMessage);
        ChatMessageResponseDto responseDto = new ChatMessageResponseDto(chatMessage.getRoom().getRoomId(), chatMessage.getSender().getNickName(), chatMessage.getContent(), changeDateFormat(chatMessage.getCreatedAt()));
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), responseDto);
    }

    // Company
    private MyChatRoomResponseDto findCompanyMessageHistoryByRoomId(String roomId, Company company) {
        ChatRoom room = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND));
        List<MessageListDto> messages = chatMessageRepository.findAllByRoom(room).stream()
                .map(m -> new MessageListDto(m.getSender().getNickName(), m.getContent(), m.getRoom().getRoomId(), changeDateFormat(m.getCreatedAt()))).toList();
        List<ChatRoom> chatRooms = chatRoomRepository.findAllChatRoomByCompany(company);
        List<RoomListDto> rooms = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            boolean target = chatRoom.getRoomId().equals(roomId);
            User other = chatRoom.getUser();
            List<ChatMessage> chatMessages = chatMessageRepository.findLastMessageByRoom(chatRoom);
            String lastMessage = chatMessages.size() > 0 ? chatMessages.get(0).getContent() : null;
            rooms.add(new RoomListDto(chatRoom.getRoomId(), other.getNickName(), null, lastMessage, target));
        }
        return new MyChatRoomResponseDto(messages, rooms);
    }

    // User
    private MyChatRoomResponseDto findUserMessageHistoryByRoomId(String roomId, User user) {
        ChatRoom room = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND));
        List<MessageListDto> messages = chatMessageRepository.findAllByRoom(room).stream()
                .map(m -> new MessageListDto(m.getSender().getNickName(), m.getContent(), m.getRoom().getRoomId(), changeDateFormat(m.getCreatedAt()))).toList();
        List<ChatRoom> chatRooms = chatRoomRepository.findAllChatRoomByUser(user);
        List<RoomListDto> rooms = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            boolean target = chatRoom.getRoomId().equals(roomId);
            Company other = chatRoom.getOwner();
            List<ChatMessage> chatMessages = chatMessageRepository.findLastMessageByRoom(chatRoom);
            String lastMessage = chatMessages.size() > 0 ? chatMessages.get(0).getContent() : null;
            rooms.add(new RoomListDto(chatRoom.getRoomId(), other.getCompanyName(), other.getLogoUrl(), lastMessage, target));
        }
        return new MyChatRoomResponseDto(messages, rooms);
    }

    private String changeDateFormat(String createdAt) {
        String[] date = createdAt.split(" ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter);
        return date[0].equals(today) ? date[1] : date[0];
    }

    public void deleteByStudyId(Long studyId) {
        chatRoomRepository.deleteByStudyId(studyId);
    }

    public void deleteMessagesByStudyId(Long studyId) {
        chatMessageRepository.deleteMessagesByStudyId(studyId);
    }

    @Transactional
    public void deleteRoom(String roomId) {
        ChatRoom room = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND));
        if (chatMessageRepository.existsByRoom(room)) {
            chatMessageRepository.deleteByRoom(room);
        }
        chatRoomRepository.deleteById(room.getId());
    }
}
