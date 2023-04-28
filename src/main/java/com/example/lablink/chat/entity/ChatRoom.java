package com.example.lablink.chat.entity;

import com.example.lablink.company.entity.Company;
import com.example.lablink.study.entity.Study;
import com.example.lablink.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @ManyToOne
    private Study study;

    @ManyToOne
    private User user;

    @ManyToOne
    private Company owner;

    public ChatRoom(Study study, User user, Company owner) {
        this.roomId = UUID.randomUUID().toString().substring(0, 8);
        this.study = study;
        this.user = user;
        this.owner = owner;
    }
}
