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
    private final boolean isbookmarked;
    // todo : 하트 한번 더 누르면 북마크 삭제되게 만들깅

    public BookmarkResponseDto(Study study, Bookmark bookmark) {
        this.id = bookmark.getId();
        this.isbookmarked = true;
        this.title = study.getTitle();
        this.category = study.getCategory();
        this.date = study.getDate();
        this.address = study.getAddress();
        this.pay = study.getPay();
        this.companyName = study.getCompany().getCompanyName();
    }
}
