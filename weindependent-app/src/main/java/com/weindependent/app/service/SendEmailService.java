package com.weindependent.app.service;

public interface SendEmailService {
    boolean send(int templateId, String email);
}
