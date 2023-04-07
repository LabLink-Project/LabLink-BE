package com.example.lablink.timestamp.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Timestamped {
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
//    @DateTimeFormat(pattern="yyyy-MM-dd")
    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedDate
    private LocalDateTime modifiedAt;

    @CreatedDate
    private LocalDateTime deletedAt;
}