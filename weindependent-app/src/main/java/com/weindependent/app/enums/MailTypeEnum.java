package com.weindependent.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailTypeEnum {
    RESET_PASSWORD("classpath:email/reset-password.html", "Reset Your We Independent Password"),
    REGISTER_EVENT("classpath:email/register-event.html", "Event Registration Confirmed"),
    HELP_CENTER("classpath:email/help-center.html", "Your Help Request Has Been Received"),
    HELP_CENTER_NOTIFY("classpath:email/help-center-admin.html", "New Help Request Submitted");

    private final String path;
    private final String subject;
}