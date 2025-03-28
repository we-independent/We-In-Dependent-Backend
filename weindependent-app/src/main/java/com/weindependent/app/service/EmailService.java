package com.weindependent.app.service;

import java.util.Map;

public interface EmailService {
    boolean send(String email, Map<String,String> sendMailParams);
}
