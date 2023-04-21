package com.example.lablink.application.dto.Response;

import com.example.lablink.application.entity.Application;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class ApplicationFromStudyResponseDto {

    private Long id;
    private String userName;
    private String userPhone;
    private String userGender;

    // 날짜 형식
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date dateOfBirth;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    private String userAddress;
    private String message;

    public ApplicationFromStudyResponseDto(User user, UserInfo userInfo , Application applicationString) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.userPhone = userInfo.getUserPhone();
        this.userGender = user.getUserGender();
        this.dateOfBirth = user.getDateOfBirth();
        this.createdAt = applicationString.getCreatedAt();
        this.userAddress = userInfo.getUserAddress();
        this.message = applicationString.getMessage();
    }
}
