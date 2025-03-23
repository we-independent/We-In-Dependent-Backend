package com.weindependent.app.service.impl;

//import com.weindependent.app.enums.EmailTemplateEnum;
import com.weindependent.app.enums.EmailTemplateEnum;
import com.weindependent.app.service.EmailService;
import com.weindependent.app.service.SendEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
public class SendEmailServiceImpl implements SendEmailService {
    @Autowired
    private ApplicationContext context;

    public boolean send(int templateId, String email, Map<String,String> sendMailParams) {
        boolean result = true;
        if (templateId == 1) {
            try {
                String className = EmailTemplateEnum.RESET_USER_PASSWORD.getEmailService();
                Class classIns = Class.forName(className);
                EmailService emailService = (EmailService) context.getBean(classIns);
                result = emailService.send(email, sendMailParams);
            } catch (Exception e) {
                log.error("Failed to send email:" + e.getMessage() + '\n'+  e.getStackTrace());
                return false;
            }
        }
        return result;
    }
}
