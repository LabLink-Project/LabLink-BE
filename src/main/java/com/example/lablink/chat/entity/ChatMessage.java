package com.example.lablink.chat.entity;

import com.example.lablink.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime sendTime;

//    @ManyToOne
//    @JoinColumn(name = "sender_id")
//    private User sender;
//
//    @ManyToOne
//    @JoinColumn(name = "room_id")
//    private ChatRoom chatRoom;

    @Column(nullable = false)
    private long writer;

    @Column(nullable = false)
    private long roomId;

}

