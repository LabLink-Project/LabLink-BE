package com.example.lablink.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyResponseDto {
    private String userName;
    private String dateOfBirth;

    public UserModifyResponseDto(String userName, Date dateOfBirth) {
        this.userName = userName;
        this.dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(dateOfBirth);
    }
}
