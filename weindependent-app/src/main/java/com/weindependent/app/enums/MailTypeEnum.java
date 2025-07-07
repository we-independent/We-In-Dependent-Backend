package com.weindependent.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailTypeEnum {
    RESET_PASSWORD("classpath:email/reset-password.html", "Reset Your We Independent Password"),
    REGISTER_EVENT("classpath:email/register-event.html", "Event Registration Confirmed"),
    HELP_CENTER("classpath:email/help-center.html", "Your Help Request Has Been Received"),
    HELP_CENTER_NOTIFY("classpath:email/help-center-admin.html", "New Help Request Submitted"),
    UPDATES_GENERAL("classpath:email/general-updates.html", "General Announcement from We Independent"),
    UPDATES_PROGRAMS_OR_FEATURES("classpath:email/program-updates.html", "New Program or Feature Update from We Independent"),
    UPDATES_HOLIDAY("classpath:email/holiday-messages-updates.html", "Holiday Message from We Independent"),
    UPDATES_MONTHLY_HIGHLIGHT("classpath:email/monthly-highlight.html", "Our Montly Highlight from We Independent!");
    
    private final String path;
    private final String subject;
}