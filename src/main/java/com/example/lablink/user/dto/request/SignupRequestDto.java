package com.example.lablink.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

    private boolean ageCheck;
    private boolean termsOfServiceAgreement;
    private boolean privacyPolicyConsent;
    private boolean sensitiveInfoConsent;
    private boolean marketingOptIn;

}
