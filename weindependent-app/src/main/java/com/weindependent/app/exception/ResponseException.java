package com.weindependent.app.exception;

import com.weindependent.app.enums.ErrorCode;
import lombok.Data;

@Data
public class ResponseException extends RuntimeException {
    private int errorCode;
    private String message;

    public ResponseException(int errorCode, String message){
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public ResponseException(int errorCode, String message, Throwable e){
        super(message, e);
        this.message = message;
        this.errorCode = errorCode;
    }

    public ResponseException(ErrorCode errorCode, String message){
        super(message);
        this.message = message;
        this.errorCode = errorCode.getCode();
    }

    public ResponseException(ErrorCode errorCode, String message, Throwable e){
        super(message, e);
        this.message = message;
        this.errorCode = errorCode.getCode();
    }
}
