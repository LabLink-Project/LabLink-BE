package com.example.lablink.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UserModifyResponseDto {
    private String userName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dateOfBirth;

    public UserModifyResponseDto(String userName, Date dateOfBirth) {
        this.userName = userName;
        this.dateOfBirth = dateOfBirth;
    }
}
