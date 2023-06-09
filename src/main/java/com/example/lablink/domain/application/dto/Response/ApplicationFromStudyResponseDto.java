package com.example.lablink.domain.application.dto.Response;

import com.example.lablink.domain.application.entity.Application;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.entity.UserInfo;
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

    public ApplicationFromStudyResponseDto(User user, UserInfo userInfo , Application application) {
        this.id = application.getId();
        this.userName = user.getUserName();
        this.userPhone = userInfo.getUserPhone();
        this.userGender = user.getUserGender();
        this.dateOfBirth = user.getDateOfBirth();
        this.createdAt = application.getCreatedAt();
        this.userAddress = userInfo.getUserAddress();
        this.message = application.getMessage();
        this.approvalStatusEnum = application.getApprovalStatusEnum();
    }
}
