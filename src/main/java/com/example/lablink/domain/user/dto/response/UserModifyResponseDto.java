package com.example.lablink.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserModifyResponseDto {
    private String userName;

    // TODO 데이트타입 통일화 현재 약 4개정도 사용중
    //-> request @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") 해서 저장하고
    //-> response @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate dateOfBirth;

    public UserModifyResponseDto(String userName, LocalDate dateOfBirth) {
        this.userName = userName;
        this.dateOfBirth = dateOfBirth;
    }
}
