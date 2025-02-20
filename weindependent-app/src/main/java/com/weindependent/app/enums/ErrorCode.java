package com.weindependent.app.enums;

public enum ErrorCode {
    SUCCESS(0, "请求成功"),
    INVALID_PARAM(1, "参数异常"),
    SIGN_AUTH_FAILED(2, "签名验证失败"),
    NON_VERSION(3, "接口版本号不能为空"),
    NON_TIMESTAMP(4, "时间戳不能为空"),
    NON_SIGN(5, "签名不能为空"),
    UNDEFINED_ERROR(6, "未知错误"),
    INTERNAL_SERVER_ERROR(100, "服务器内部错误");

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
