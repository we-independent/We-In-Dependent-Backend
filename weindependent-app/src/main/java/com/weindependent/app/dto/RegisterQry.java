package com.weindependent.app.dto;

import lombok.Data;

@Data
public class RegisterQry {
    private String account;
    private String password;
    private String realName;
    private String language;
    private String visaType;
    private boolean subscription;
}
