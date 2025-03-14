package com.weindependent.app.enums;

import com.weindependent.app.service.EmailService;
import com.weindependent.app.service.impl.ResetUserPasswordEmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

public enum EmailTemplateEnum {
    RESET_USER_PASSWORD(1) {
        @Override
        public String getEmailService() {
            return ResetUserPasswordEmailServiceImpl.class.getName();
        }
    };

    private int id;

    private EmailTemplateEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public abstract String getEmailService();
}
