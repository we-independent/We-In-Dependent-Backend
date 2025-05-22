package com.weindependent.app.database.dataobject;

import lombok.Data;
//Author : Nero


@Data
public class UserDO {
    private Long id;
    private String account;
    private String password;
    private String realName;
    private String email;
    private Integer profileImageId;
    private String loginProvider;
    private String language;
    private String visaType;
    private boolean subscription;
    private Boolean isVisaPublic;
}
