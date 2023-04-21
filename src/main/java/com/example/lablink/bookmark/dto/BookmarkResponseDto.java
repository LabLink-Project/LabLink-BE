package com.example.lablink.bookmark.dto;

import com.example.lablink.bookmark.entity.Bookmark;
import com.example.lablink.study.entity.CategoryEnum;
import com.example.lablink.study.entity.Study;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class BookmarkResponseDto {
    // title, company, pay, date, category, address

    private final Long id;
    private final String title;
    private final CategoryEnum category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime date;
    private final String address;
    private final int pay;
    private final String companyName;

    public BookmarkResponseDto(Study study, long bookmarkId) {
        this.id = bookmarkId;
        this.title = study.getTitle();
        this.category = study.getCategory();
        this.date = study.getDate();
        this.address = study.getAddress();
        this.pay = study.getPay();
        this.companyName = study.getCompany().getCompanyName();
    }
}
