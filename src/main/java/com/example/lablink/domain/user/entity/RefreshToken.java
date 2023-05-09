package com.example.lablink.domain.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class) // db에 createdAt 생성해주기
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // RefreshToken ID
    private String token;       // RefreshToken 값
    private Long tokenIndex;
    @CreatedDate
    private LocalDateTime createdAt;

    public RefreshToken(Long tokenIndex, String token) {
        this.tokenIndex = tokenIndex;
        this.token = token;
    }

}
