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
    BLOG_NOT_EXIST(-4, "Blog does not exist"),
    USERNAME_PASSWORD_ERROR(401, "Invalid username or password"),
    NOT_LOGGED_IN(402, "Login expired or not logged in"),
    UNAUTHORIZED_ACCESS(403, "You are not allowed to access this resource."),
    FONT_NOT_EXIST(-5, "Font not exist"),
    RELATED_ARTICLE_FETCH_FAILED(-5, "Fail to Get Related Articles"), 
    UNKONWN_CATEGORY(-6, "Unknown Category"),
    EVENT_NOT_EXIST(-7, "Event does not exist"),
    SPEAKER_NOT_EXIST(-8, "Speaker does not exist"),
    NOTIFICATION_DISABLED(-9, "Notification is not allowed for this field by user"),
    UNKNOWN_NOTIFICATION_TYPE(-10, "Unknown nofitification"),
    MESSAGE_NOT_EXIST(-11, "Notification message does not exist"),
    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Resource not found"),
    IPN_INVALID(-9, "Received Paypal IPN message was not valid"),
    SEND_MAIL_FAILED(-10, "Failed to send email");
    
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
