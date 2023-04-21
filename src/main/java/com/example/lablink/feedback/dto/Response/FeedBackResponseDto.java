package com.example.lablink.feedback.dto.Response;


import com.example.lablink.feedback.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedBackResponseDto {

    private Long id;

    private String userName;
    private String userEmail;
    private String userGender;
    private String userPhone;
    private String feedbackMessage;
    private boolean viewStatus;


    public FeedBackResponseDto(Feedback feedback) {
        this.id = feedback.getId();
        this.userName = feedback.getUser().getUserName();
        this.userEmail = feedback.getUser().getEmail();
        this.userGender=feedback.getUser().getUserGender();
        this.userPhone=feedback.getUser().getUserinfo().getUserPhone();
        this.feedbackMessage = feedback.getFeedbackMessage();
        this.viewStatus = feedback.isViewStatus();
    }


}
