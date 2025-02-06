package com.weindependent.app.exception;


import com.weindependent.app.enums.ErrorCode;

public class SignatureAuthException extends RuntimeException {
    private static final long serialVersionUID = 1589405617157165466L;
    private int code;
    private String message;

    public SignatureAuthException(ErrorCode errorCode) {
        super();
        this.code = errorCode.getCode();
        this.message = errorCode.getTitle();

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
