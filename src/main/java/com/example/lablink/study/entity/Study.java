package com.example.lablink.study.entity;

import com.example.lablink.company.entity.Company;
import com.example.lablink.study.dto.requestDto.StudyRequestDto;
import com.example.lablink.timestamp.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
    private int subjectMinAge;

    @Column(nullable = false)
    private int subjectMaxAge;

    @Column(nullable = false)
    private int repearCount;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StudyStatusEnum status;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CategoryEnum category;

    @Column(nullable = false)
    private int currentApplicantCount; // 지원자 현황

    public Study(StudyRequestDto requestDto, StudyStatusEnum status, Company company, String storedFileName) {
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
        this.subjectMinAge = requestDto.getSubjectMinAge();
        this.subjectMaxAge = requestDto.getSubjectMaxAge();
        this.repearCount = requestDto.getRepearCount();
        this.endDate = requestDto.getEndDate();
        // done: 이미지 null값일 시 썸네일 넣어주기
        this.imageURL = Objects.requireNonNullElse(storedFileName, "https://cdn.icon-icons.com/icons2/931/PNG/512/empty_file_icon-icons.com_72420.png");
        this.status = status;
        this.category = requestDto.getCategory();
        this.currentApplicantCount = 0;
    }

    public void update(StudyRequestDto requestDto, StudyStatusEnum status, String storedFileName) {
        this.title = requestDto.getTitle();
        this.studyInfo = requestDto.getStudyInfo();
        this.studyPurpose = requestDto.getStudyPurpose();
        this.studyAction = requestDto.getStudyAction();
        this.subjectCount = requestDto.getSubjectCount();
        this.date = requestDto.getDate();
        this.address = requestDto.getAddress();
        this.pay = requestDto.getPay();
        this.subjectGender = requestDto.getSubjectGender();
        this.subjectMinAge = requestDto.getSubjectMinAge();
        this.subjectMaxAge = requestDto.getSubjectMaxAge();
        this.repearCount = requestDto.getRepearCount();
        this.endDate = requestDto.getEndDate();
        if (storedFileName != null) this.imageURL = storedFileName;
        this.status = status;
    }

    public void updateStatus(StudyStatusEnum status) {
        this.status = status;
    }

    public void updateCurrentApplicantCount() {
        ++this.currentApplicantCount;
    }
}
