package com.weindependent.app.service;

import com.weindependent.app.enums.MailTypeEnum;

import java.util.Map;

public interface IEmailService {
    boolean send(String email, MailTypeEnum mailTypeEnum, Map<String,String> sendMailParams);
}
