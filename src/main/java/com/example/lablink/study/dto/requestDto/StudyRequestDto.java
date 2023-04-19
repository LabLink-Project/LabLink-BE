package com.example.lablink.study.dto.requestDto;
import com.example.lablink.study.entity.CategoryEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class StudyRequestDto {
    private String title;
    private String studyInfo;
    private String studyPurpose;
    private String studyAction;
    private Long subjectCount;
    private CategoryEnum category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    private String address;
    private int pay;
    private String subjectGender;
    private int subjectMinAge;
    private int subjectMaxAge;
    private int repearCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;
    MultipartFile image;
}
