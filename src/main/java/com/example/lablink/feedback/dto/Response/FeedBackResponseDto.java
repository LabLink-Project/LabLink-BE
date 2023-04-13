package com.example.lablink.feedback.dto.Response;

import com.example.lablink.company.entity.Company;
import com.example.lablink.feedback.entity.Feedback;
import com.example.lablink.study.entity.Study;
import com.example.lablink.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FeedBackResponseDto {

    private Long id;

    private String userName;

    private String userEmail;

    private String feedbackMessage;

    public FeedBackResponseDto(Feedback feedback) {
        this.id = feedback.getId();
        this.userName = feedback.getUser().getUserName();
        this.userEmail = feedback.getUser().getEmail();
        this.feedbackMessage = feedback.getFeedbackMessage();
    }


}
