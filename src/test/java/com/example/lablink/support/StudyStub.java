package com.example.lablink.support;

import com.example.lablink.company.entity.Company;
import com.example.lablink.study.entity.CategoryEnum;
import com.example.lablink.study.entity.Study;
import com.example.lablink.study.entity.StudyStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public enum StudyStub {
    Study1(1L, "Study1 Title", "Example Study Info", "Example Study Purpose", "Example Study Action", 50L, CategoryEnum.ONLINE, LocalDateTime.now(), "Example Address", 50000, "MALE", 20, 50, 3, LocalDateTime.now().plusDays(30), "http://example.com/image.jpg", true, 10),
    Study2(2L, "Study2 Title", "Example Study Info", "Example Study Purpose", "Example Study Action", 50L, CategoryEnum.ONLINE, LocalDateTime.now(), "Example Address", 50000, "MALE", 20, 50, 3, LocalDateTime.now().plusDays(30), "http://example.com/image.jpg", true, 10);
    private final Long id;
    private final String title;
    private final String studyInfo;
    private final String studyPurpose;
    private final String studyAction;
    private final Long subjectCount;
    private final CategoryEnum category;
    private final LocalDateTime date;
    private final String address;
    private final int pay;
    private final String subjectGender;
    private final int subjectMinAge;
    private final int subjectMaxAge;
    private final int repearCount;
    private final LocalDateTime endDate;
    private final String imageURL;
    private final boolean isbookmarked;
    private final int currentApplicantCount;

    StudyStub(Long id, String title, String studyInfo, String studyPurpose, String studyAction, Long subjectCount, CategoryEnum category, LocalDateTime date, String address, int pay, String subjectGender, int subjectMinAge, int subjectMaxAge, int repearCount, LocalDateTime endDate, String imageURL, boolean isbookmarked, int currentApplicantCount) {
        this.id = id;
        this.title = title;
        this.studyInfo = studyInfo;
        this.studyPurpose = studyPurpose;
        this.studyAction = studyAction;
        this.subjectCount = subjectCount;
        this.category = category;
        this.date = date;
        this.address = address;
        this.pay = pay;
        this.subjectGender = subjectGender;
        this.subjectMinAge = subjectMinAge;
        this.subjectMaxAge = subjectMaxAge;
        this.repearCount = repearCount;
        this.endDate = endDate;
        this.imageURL = imageURL;
        this.isbookmarked = isbookmarked;
        this.currentApplicantCount = currentApplicantCount;
    }

    public Study of(Long studyId){
        return Study.builder()
                .id(studyId)
                .company(new Company())
                .title(this.title)
                .studyInfo(this.studyInfo)
                .studyPurpose(this.studyPurpose)
                .studyAction(this.studyAction)
                .subjectCount(this.subjectCount)
                .date(this.date)
                .address(this.address)
                .pay(this.pay)
                .subjectGender(this.subjectGender)
                .subjectMinAge(this.subjectMinAge)
                .subjectMaxAge(this.subjectMaxAge)
                .repearCount(1)
                .endDate(this.endDate)
                .imageURL(this.imageURL)
                .status(StudyStatusEnum.ONGOING)
                .category(this.category)
                .currentApplicantCount(this.currentApplicantCount)
                .build();
    }


//    public Study of(Long studyId){
//        return Study.builder()
//                .id(studyId)
//                .company(new Company())
//                .title("test study")
//                .studyInfo("test study info")
//                .studyPurpose("test study purpose")
//                .studyAction("test study action")
//                .subjectCount(10L)
//                .date(LocalDateTime.now())
//                .address("test address")
//                .pay(50000)
//                .subjectGender("male")
//                .subjectMinAge(20)
//                .subjectMaxAge(30)
//                .repearCount(2)
//                .endDate(LocalDateTime.now().plusDays(7))
//                .imageURL("https://example.com/image.jpg")
//                .status(StudyStatusEnum.ONGOING)
//                .category(CategoryEnum.ONLINE)
//                .currentApplicantCount(0)
//                .build();
//    }
}
