package com.example.lablink.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum UserErrorCode {
    INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다"), // 401
    DUPLICATE_EMAIL(CONFLICT, "중복된 이메일이 존재합니다"), // 409
    EMAIL_NOT_FOUND(NOT_FOUND, "존재하지 않는 이메일 입니다."), // 404
    PASSWORD_MISMATCH(BAD_REQUEST, "비밀번호가 일치하지 않습니다."), // 400
    DUPLICATE_PASSWORD(BAD_REQUEST, "비밀번호가 동일합니다."),
//    NOT_PROPER_INPUTFORM(BAD_REQUEST, "입력한 형식이 맞지 않습니다."),
//    NOT_HAVE_PERMISSION(BAD_REQUEST, "권한이 없습니다."),
//    NOT_AUTHOR(BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
//
//    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "등록된 사용자가 없습니다"),
    USERINFO_NOT_FOUND(NOT_FOUND, "등록된 사용자 정보가 없습니다"),
//    POST_NOT_FOUND(NOT_FOUND, "선택한 게시물을 찾을 수 없습니다."),
//    COMMENT_NOT_FOUND(NOT_FOUND, "선택한 댓글을 찾을 수 없습니다.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}