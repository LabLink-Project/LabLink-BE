package com.example.lablink.user.entity;

public enum UserRoleEnum {
    USER(Authority.USER),  // 고객 권한
    BUSINESS(Authority.BUSINESS);  // 사업자 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String BUSINESS = "ROLE_BUSINESS";
    }
}
