package com.example.lablink.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TermsRequestDto {
    @AssertTrue(message = "필수약관 동의가 필요합니다.")
    private boolean ageCheck;
    @AssertTrue(message = "필수약관 동의가 필요합니다.")
    private boolean termsOfServiceAgreement;
    @AssertTrue(message = "필수약관 동의가 필요합니다.")
    private boolean privacyPolicyConsent;
    @AssertTrue(message = "필수약관 동의가 필요합니다.")
    private boolean sensitiveInfoConsent;
    private boolean marketingOptIn;
}
