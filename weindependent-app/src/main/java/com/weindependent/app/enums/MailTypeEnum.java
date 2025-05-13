package com.weindependent.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailTypeEnum {
    RESET_PASSWORD("classpath:email/reset-password.html", "Reset Your We Independent Password"),
    REGISTER_EVENT("classpath:email/register-event.html", "Event Registration Confirmed");

    private final String path;
    private final String subject;
}