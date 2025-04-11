package com.weindependent.app.service;

import java.util.Map;

public interface GoogleOAuthService {
    public Map<String, String> exchangeCodeForUserInfo(String code);
}
