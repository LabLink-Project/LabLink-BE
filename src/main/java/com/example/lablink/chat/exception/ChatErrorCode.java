package com.example.lablink.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ChatErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),

    /* 403 FORBIDDEN : 권한 */

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    CHATROOM_NOT_FOUND(NOT_FOUND, "선택한 채팅방을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
