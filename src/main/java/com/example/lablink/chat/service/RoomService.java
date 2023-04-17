package com.example.lablink.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
//    private final UserService userService;
//    private final RoomRepository roomRepository;
//
//    public Long createRoom(long receiverId, UserDetailsImpl userDetails) {
//        User receiver = userService.findUser(receiverId);
//        User sender = userService.findUser(userDetails.getUser().getId());
//
//        // 둘의 채팅이 있는 지 확인
//        Optional<ChatRoom> optionalChatRoom = roomRepository.findBySenderAndReceiver(sender, receiver);
//        Optional<ChatRoom> optionalChatRoom2 = roomRepository.findBySenderAndReceiver(receiver, sender);
//
//        ChatRoom chatRoom = null;
//
//        if(optionalChatRoom.isPresent()) {
//            chatRoom = optionalChatRoom.get();
//            log.info("find chat room");
//            return chatRoom.getRoomId();
//        } else if (optionalChatRoom2.isPresent()) {
//            chatRoom = optionalChatRoom2.get();
//            log.info("find chat room");
//            return chatRoom.getRoomId();
//        } else {
//            chatRoom = ChatRoom.builder().sender(sender).receiver(receiver).build();
//            log.info("create chat room");
//        }
//
//        ChatRoom saveChatRoom = roomRepository.save(chatRoom);
//
//        return saveChatRoom.getRoomId();
//    }
//
//    // 유저의 채팅 목록 가져오기
//    public Page<ChatRoom> findRooms(UserDetailsImpl userDetails, int page, int size) {
//        User sender = userService.findUser(userDetails.getUser().getId());
//        Pageable pageable = PageRequest.of(page-1 , size, Sort.by("roomId").descending());
//        Page<ChatRoom> chatRooms = roomRepository.findAllBySenderOrReceiver(pageable, sender, sender);
//
//        return chatRooms;
//    }
//
//    // 채팅방 하나 찾기
//    public ChatRoom findRoom(long roomId) {
//        ChatRoom chatRoom = findExistRoom(roomId);
//
//        return chatRoom;
//    }
//
//    // 채팅방 존재 검증
//    private ChatRoom findExistRoom(long roomId) {
//        Optional<ChatRoom> optionalChatRoom = roomRepository.findById(roomId);
//
//        ChatRoom findChatRoom = optionalChatRoom.orElseThrow(
//                () -> new StudyException(StudyErrorCode.STUDY_NOT_FOUND)
//        );
//
//        return findChatRoom;
//    }

}
