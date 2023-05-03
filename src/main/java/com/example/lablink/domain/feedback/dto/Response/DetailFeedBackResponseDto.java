package com.example.lablink.domain.feedback.dto.Response;

import com.example.lablink.domain.feedback.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetailFeedBackResponseDto {

    private Long id;

    private String userName;

    private String userEmail;

    private String feedbackMessage;



    public DetailFeedBackResponseDto(Feedback feedback) {
        this.id = feedback.getId();
        this.userName = feedback.getUser().getUserName();
        this.userEmail = feedback.getUser().getEmail();
        this.feedbackMessage = feedback.getFeedbackMessage();

    }
}
