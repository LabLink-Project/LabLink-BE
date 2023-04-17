package com.example.lablink.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    /* 유효성 검사
    * Valid -> 한번에 오류보냄 (그냥 오류났다~)
    * 글로벌(서비스에서 지정한 Exception마다 하나씩 비교&출력 가능) -> 각 컬럼별 오류메세지 출력 가능(프론트가 뺴서 쓰기 쉬움)
    @NotBlank
    * */
    private String email;
    private String password;
    private String userPhone;

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
