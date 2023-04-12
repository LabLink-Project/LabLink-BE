package com.example.lablink.feedback.entity;

import com.example.lablink.study.entity.Study;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Feedback  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private Long user_id;

    @Column(nullable = false)
    private String FeedBackMessage;

    @Column(nullable = false)
    private boolean management;

    @ManyToOne
    @JoinColumn(name = "study_id",nullable = false)
    private Study study;

}
