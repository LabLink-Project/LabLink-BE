package com.example.lablink.user.entity;

import com.example.lablink.user.dto.request.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean marketingTerm;

    @Column(nullable = false)
    private boolean notificationTerm;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Terms(SignupRequestDto signupRequestDto, User user) {
        this.marketingTerm = signupRequestDto.isMarketingTerm();
        this.notificationTerm = signupRequestDto.isNotificationTerm();
        this.user = user;
    }
}
