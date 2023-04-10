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

    @Column(nullable = false, unique = true)
    private boolean ageCheck;

    @Column(nullable = false, unique = true)
    private boolean termsOfServiceAgreement;

    @Column(nullable = false, unique = true)
    private boolean privacyPolicyConsent;

    @Column(nullable = false, unique = true)
    private boolean sensitiveInfoConsent;

    @Column(nullable = false)
    private boolean marketingOptIn;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Terms(SignupRequestDto signupRequestDto, User user) {
        this.ageCheck = signupRequestDto.isAgeCheck();
        this.termsOfServiceAgreement = signupRequestDto.isTermsOfServiceAgreement();
        this.privacyPolicyConsent = signupRequestDto.isPrivacyPolicyConsent();
        this.sensitiveInfoConsent = signupRequestDto.isSensitiveInfoConsent();
        this.marketingOptIn = signupRequestDto.isMarketingOptIn();
        this.user = user;
    }
}
