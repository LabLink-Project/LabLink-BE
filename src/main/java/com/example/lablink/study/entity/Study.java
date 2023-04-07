package com.example.lablink.study.entity;

import com.example.lablink.company.entity.Company;
import com.example.lablink.study.dto.requestDto.StudyRequestDto;
import com.example.lablink.timestamp.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Study extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String studyInfo;

    @Lob
    @Column(nullable = false)
    private String studyPurpose;

    @Lob
    @Column(nullable = false)
    private String studyAction;

    @Column(nullable = false)
    private Long subjectCount;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int pay;

    @Column(nullable = false)
    private String subjectGender;

    @Column(nullable = false)
    private String subjectAge;

    @Column(nullable = false)
    private int repearCount;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StudyStatusEnum status;

    public Study(StudyRequestDto requestDto, Company company) {
        this.title = requestDto.getTitle();
        this.company = company;
        this.studyInfo = requestDto.getStudyInfo();
        this.studyPurpose = requestDto.getStudyPurpose();
        this.studyAction = requestDto.getStudyAction();
        this.subjectCount = requestDto.getSubjectCount();
        this.date = requestDto.getDate();
        this.address = requestDto.getAddress();
        this.pay = requestDto.getPay();
        this.subjectGender = requestDto.getSubjectGender();
        this.subjectAge = requestDto.getSubjectAge();
        this.repearCount = requestDto.getRepearCount();
        this.endDate = requestDto.getEndDate();
        this.imageURL = requestDto.getImageURL();
        this.status = StudyStatusEnum.ONGOING;
    }

    // xxx : test
    public Study(StudyRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.studyInfo = requestDto.getStudyInfo();
        this.studyPurpose = requestDto.getStudyPurpose();
        this.studyAction = requestDto.getStudyAction();
        this.subjectCount = requestDto.getSubjectCount();
        this.date = requestDto.getDate();
        this.address = requestDto.getAddress();
        this.pay = requestDto.getPay();
        this.subjectGender = requestDto.getSubjectGender();
        this.subjectAge = requestDto.getSubjectAge();
        this.repearCount = requestDto.getRepearCount();
        this.endDate = requestDto.getEndDate();
        this.imageURL = requestDto.getImageURL();
        this.status = StudyStatusEnum.ONGOING;
    }

    public void update(StudyRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.studyInfo = requestDto.getStudyInfo();
        this.studyPurpose = requestDto.getStudyPurpose();
        this.studyAction = requestDto.getStudyAction();
        this.subjectCount = requestDto.getSubjectCount();
        this.date = requestDto.getDate();
        this.address = requestDto.getAddress();
        this.pay = requestDto.getPay();
        this.subjectGender = requestDto.getSubjectGender();
        this.subjectAge = requestDto.getSubjectAge();
        this.repearCount = requestDto.getRepearCount();
        this.endDate = requestDto.getEndDate();
        this.imageURL = requestDto.getImageURL();
    }

    public void updateStatus(StudyStatusEnum status) {
        this.status = status;
    }
}
