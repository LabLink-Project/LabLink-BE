package com.example.lablink.feedback.entity;

import com.example.lablink.study.entity.Study;
import com.example.lablink.timestamp.entity.Timestamped;
import com.example.lablink.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "study_id",nullable = false)
    private Study study;

    @Column(nullable = false)
    private String feedbackMessage;

    @Column(nullable = false)
    private boolean management;

    @CreatedDate
    private LocalDateTime createdAt;

    public Feedback(User user, Study study, String feedbackMessage, boolean management) {
        this.user = user;
        this.study = study;
        this.feedbackMessage = feedbackMessage;
        this.management = management;
    }

    public void update(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

}
