package com.example.lablink.application.dto.Response;

import com.example.lablink.application.entity.Application;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ApplicationFromStudyResponseDto {

    private final Long id;
    private final String userName;
    private final String userPhone;
    private final String userGender;

    // 날짜 형식
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate dateOfBirth;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime createdAt;

    private final String userAddress;
    private final String message;
    private final String approvalStatusEnum;

    public ApplicationFromStudyResponseDto(User user, UserInfo userInfo , Application applicationString) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.userPhone = userInfo.getUserPhone();
        this.userGender = user.getUserGender();
        this.dateOfBirth = user.getDateOfBirth();
        this.createdAt = applicationString.getCreatedAt();
        this.userAddress = userInfo.getUserAddress();
        this.message = applicationString.getMessage();
        this.approvalStatusEnum = applicationString.getApprovalStatusEnum();
    }
}
