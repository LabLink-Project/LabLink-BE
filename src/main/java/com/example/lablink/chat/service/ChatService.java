package com.example.lablink.chat.service;

import com.example.lablink.chat.dto.ChatMessageDTO;
import com.example.lablink.chat.entity.ChatMessage;
import com.example.lablink.chat.entity.ChatRoom;
import com.example.lablink.chat.mapper.ChatMapper;
import com.example.lablink.chat.repository.MessageRepository;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
//    private final UserService userService;
//    private final ChatRoomService chatRoomService;
    private final MessageRepository messageRepository;
    private final ChatMapper mapper;

    @Transactional
    public void saveChatContent(long roomId, String content, long writer) {
//        User user = userService.findUser(writer);
//        ChatRoom chatRoom = chatRoomService.getChatRoom(roomId);

        ChatMessage chatMessage = ChatMessage
                .builder()
                .message(content)
                .writer(writer)
                .roomId(roomId)
                .sendTime(LocalDateTime.now())
                .build();

        messageRepository.save(chatMessage);
        log.info("메세지 저장 완료");
    }

    public List<ChatMessageDTO> getChatContent(long roomId) {
        List<ChatMessage> chatMessages = messageRepository.findByRoomId(roomId);
        List<ChatMessageDTO> chatMessageDTOS = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            chatMessageDTOS.add(new ChatMessageDTO(chatMessage));
        }
        return chatMessageDTOS;
//        return mapper.messagesToMessageResponseDtos(chatMessages);
    }
}
