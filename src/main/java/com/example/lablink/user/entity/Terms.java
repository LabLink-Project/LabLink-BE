package com.example.lablink.user.entity;

import com.example.lablink.user.dto.request.TermsRequestDto;
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

    public Terms(TermsRequestDto termsRequestDto) {
        this.marketingTerm = termsRequestDto.isMarketingTerm();
        this.notificationTerm = termsRequestDto.isNotificationTerm();
    }
}
