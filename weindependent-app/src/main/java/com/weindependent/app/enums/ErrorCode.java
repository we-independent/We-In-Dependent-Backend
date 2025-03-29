package com.weindependent.app.enums;

public enum ErrorCode {
    SUCCESS(0, "success"),
    INVALID_PARAM(1, "Parameter exception"),
    SIGN_AUTH_FAILED(2, "Signature verification failed"),
    NON_VERSION(3, "The interface version number cannot be empty"),
    NON_TIMESTAMP(4, "Timestamp cannot be empty"),
    NON_SIGN(5, "The signature cannot be empty"),
    UNDEFINED_ERROR(6, "Unknown error"),
    INTERNAL_SERVER_ERROR(100, "Server internal error"),
    TOKEN_NOT_EXIST_OR_EXPIRED(-1, "Token does not exist or expired"),
    USER_NOT_EXIST(-2, "User does not exist"),
    UPDATE_DB_FAILED(-3, "Update db failed"),
    USERNAME_PASSWORD_ERROR(401, "Invalid username or password");

    private int code;
    private final String title;

    private ErrorCode(int code, String title) {
        this.code = code;
        this.title = title;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }
}
