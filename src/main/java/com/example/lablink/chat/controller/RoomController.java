package com.example.lablink.chat.controller;

import com.example.lablink.chat.dto.ChatMessageDTO;
import com.example.lablink.chat.dto.ChatRoomDTO;
import com.example.lablink.chat.service.ChatRoomService;
import com.example.lablink.chat.service.ChatService;
import com.example.lablink.company.security.CompanyDetailsImpl;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequiredArgsConstructor
//@RequestMapping("/company/{companyId}")
@Log4j2
public class RoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

//    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 목록 조회
    @GetMapping("/company/{companyId}/chat/rooms")
    public ModelAndView getAllChatRooms(@PathVariable long companyId, @AuthenticationPrincipal CompanyDetailsImpl companyDetails){

        log.info("# All Chat Rooms");
        ModelAndView mv = new ModelAndView("chat/rooms");
        List<ChatRoomDTO> rooms = chatRoomService.getAllChatRooms(companyId, companyDetails);
        mv.addObject("list", rooms);

        return mv;
    }

    // 채팅방 개설
    @PostMapping("/company/{companyId}/chat/room")
    public String createChatRoom(RedirectAttributes rttr, @PathVariable Long companyId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        log.info("# Create Chat Room , name: " + companyId);
        Long roomId = chatRoomService.createChatRoom(companyId, userDetails);
        rttr.addFlashAttribute("roomName", roomId);
        return "redirect:/company/{companyId}/chat/rooms";
    }

    // 채팅방 조회
    // 채팅 내용을 불러오는 요청을 처리하는 핸들러 메소드
    // todo: 조회할 때 유저 이름 보여주기
    @GetMapping("/chat/room")
    public void getChatRoom(long roomId, Model model, @AuthenticationPrincipal CompanyDetailsImpl companyDetails, @AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("# get Chat Room, roomID : " + roomId);
        List<ChatMessageDTO> chatMessages = chatService.getChatContent(roomId);
        model.addAttribute("room", chatRoomService.getChatRoomDTO(roomId));
        model.addAttribute("chatList", chatMessages);
    }
}