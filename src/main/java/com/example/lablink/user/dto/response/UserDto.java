package com.example.lablink.user.dto.response;

import lombok.Builder;
import lombok.Getter;

public class UserDto {
    @Getter
    @Builder
    public static class ResponseOnlyUserName {
        private long userId;
        private String username;
    }
}
