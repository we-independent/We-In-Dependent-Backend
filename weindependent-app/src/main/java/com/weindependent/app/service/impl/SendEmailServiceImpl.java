package com.weindependent.app.service.impl;

//import com.weindependent.app.enums.EmailTemplateEnum;
import com.weindependent.app.enums.EmailTemplateEnum;
import com.weindependent.app.service.EmailService;
import com.weindependent.app.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class SendEmailServiceImpl implements SendEmailService {
    @Autowired
    private ApplicationContext context;

    public boolean send(int templateId, String email) {
        boolean result = true;
        if (templateId == 1) {
            try {
                String className = EmailTemplateEnum.RESET_USER_PASSWORD.getEmailService();
                Class classIns = Class.forName(className);
                EmailService emailService = (EmailService) context.getBean(classIns);
                result = emailService.send(email);
            } catch (Exception e) {
                return false;
            }
        }
        return result;
    }
}
