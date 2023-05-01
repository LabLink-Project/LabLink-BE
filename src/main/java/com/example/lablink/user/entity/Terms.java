package com.example.lablink.user.entity;

import com.example.lablink.timestamp.entity.Timestamped;
import com.example.lablink.user.dto.request.SignupRequestDto;
import com.example.lablink.user.dto.request.TermsRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
// TODO  created, modified, deleted 다 필요한가 ?
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE terms SET deleted_at = CONVERT_TZ(now(), 'UTC', 'Asia/Seoul') WHERE id = ?")
@NoArgsConstructor
public class Terms extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean ageCheck;

    @Column(nullable = false)
    private boolean termsOfServiceAgreement;

    @Column(nullable = false)
    private boolean privacyPolicyConsent;

    @Column(nullable = false)
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

    public Terms(TermsRequestDto termsRequestDto, User user) {
        this.ageCheck = termsRequestDto.isAgeCheck();
        this.termsOfServiceAgreement = termsRequestDto.isTermsOfServiceAgreement();
        this.privacyPolicyConsent = termsRequestDto.isPrivacyPolicyConsent();
        this.sensitiveInfoConsent = termsRequestDto.isSensitiveInfoConsent();
        this.marketingOptIn = termsRequestDto.isMarketingOptIn();
        this.user = user;
    }
}
