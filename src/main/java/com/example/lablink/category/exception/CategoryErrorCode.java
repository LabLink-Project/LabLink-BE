package com.example.lablink.category.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum CategoryErrorCode {
    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    CATEGORY_NOT_FOUND(NOT_FOUND, "등록된 카테고리가 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
