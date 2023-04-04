package com.example.lablink.study.entity;

import com.example.lablink.company.entity.Company;
import com.example.lablink.study.dto.requestDto.StudyRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Study {
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
    private LocalDate date;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String pay;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String age;

    @Column(nullable = false)
    private int repearCount;

    @Column(nullable = false)
    private LocalDate endDate;

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
        this.gender = requestDto.getGender();
        this.age = requestDto.getAge();
        this.repearCount = requestDto.getRepearCount();
        this.endDate = requestDto.getEndDate();
        this.imageURL = requestDto.getImageURL();
        this.status = StudyStatusEnum.ONGOING;
    }
}
