package com.example.lablink.domain.study.dto.requestDto;
import com.example.lablink.domain.study.entity.CategoryEnum;
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
    private String description;
    private String benefit; // 우대사항
    private CategoryEnum category;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    private String address;
    private Integer pay;
    private String subjectGender;
    private Integer subjectMinAge;
    private Integer subjectMaxAge;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;
    MultipartFile thumbnailImage;
    MultipartFile detailImage;
}
