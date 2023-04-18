package com.example.lablink.chat.entity;

import com.example.lablink.timestamp.entity.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    private Long userId;

    @JoinColumn(name = "company_id")
    private Long companyId;

    public ChatRoom(Long userId, Long companyId) {
        this.userId = userId;
        this.companyId = companyId;
    }
}
