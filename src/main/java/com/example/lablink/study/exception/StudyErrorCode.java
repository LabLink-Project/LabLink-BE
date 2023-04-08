package com.example.lablink.study.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum StudyErrorCode {
    // TODO : errorcode 추가

    /* 400 BAD_REQUEST : 잘못된 요청 */

    /* 401 UNAUTHORIZED : 로그인 */

    /* 403 FORBIDDEN : 권한 */
    NOT_AUTHOR(FORBIDDEN, "작성자만 삭제/수정할 수 있습니다."),
    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    STUDY_NOT_FOUND(NOT_FOUND, "등록된 공고가 없습니다"),
    NOT_FOUND_IMAGE(NOT_FOUND, "등록된 이미지가 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
