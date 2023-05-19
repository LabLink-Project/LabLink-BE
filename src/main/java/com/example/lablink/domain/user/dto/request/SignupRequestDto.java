package com.example.lablink.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    /* 유효성 검사
    * Valid -> 한번에 오류보냄 (그냥 오류났다~)
    * 글로벌(서비스에서 지정한 Exception마다 하나씩 비교&출력 가능) -> 각 컬럼별 오류메세지 출력 가능(프론트가 뺴서 쓰기 쉬움)
    * */
    @NotBlank(message = "이메일을 입력하세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$", message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,16}$", message = "한글과 영문, 숫자를 조합한 2~16자리의 닉네임을 입력하세요")
    private String nickName;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[~!@#$%^&*()_+|<>?{}\\[\\]\\\\-])(?=\\S+$).{8,20}$", message = "영문과 숫자, 특수문자를 이용한 8~20자리의 비밀번호를 입력하세요.")
    private String password;

    @NotBlank(message = "핸드폰 번호를 입력해 주세요.")
    @Pattern(regexp = "^\\d{1,11}$", message = "'-'를 제외한 숫자만 입력해주세요.")
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
