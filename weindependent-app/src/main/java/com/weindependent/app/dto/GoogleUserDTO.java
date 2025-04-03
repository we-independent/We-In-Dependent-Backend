package com.weindependent.app.dto;

import lombok.Data;

@Data
public class GoogleUserDTO {
    private Long id;
    private String account;
    private String realName;
    private String loginProvider;
    private boolean isNewUser;
    private String language;
    private String visaType;
}