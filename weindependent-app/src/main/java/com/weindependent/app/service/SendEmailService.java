package com.weindependent.app.service;

import java.util.Map;

public interface SendEmailService {
    boolean send(int templateId, String email, Map<String,String> sendMailParams);
}
