package com.example.lablink.domain.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ApplicationErrorCode {
    //    400 BAD_REQUEST : 잘못된 요청 */
    //INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다"),
    //DUPLICATE_USER(BAD_REQUEST, "중복된 사용자가 존재합니다"),
    //DUPLICATE_NICKNAME(BAD_REQUEST, "중복된 닉네임이 존재합니다"),
    //DUPLICATE_EMAIL(BAD_REQUEST, "중복된 이메일이 존재합니다"),
    //NOT_PROPER_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    //NOT_PROPER_INPUTFORM(BAD_REQUEST, "입력한 형식이 맞지 않습니다."),
    NOT_HAVE_PERMISSION(BAD_REQUEST, "권한이 없습니다."),
    NOT_AUTHOR(BAD_REQUEST, "작성자만 조회/수정/삭제할 수 있습니다."),
    NOT_MY_APPLICATION(BAD_REQUEST, "내가 작성한 신청서가 아닙니다."),
//    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    APPLICATION_NOT_FOUND(NOT_FOUND, "등록된 신청서가 없습니다"),
    STUDY_NOT_FOUND(NOT_FOUND, "등록된 공고가 없습니다"),
//    POST_NOT_FOUND(NOT_FOUND, "선택한 게시물을 찾을 수 없습니다."),
//    COMMENT_NOT_FOUND(NOT_FOUND, "선택한 댓글을 찾을 수 없습니다.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}