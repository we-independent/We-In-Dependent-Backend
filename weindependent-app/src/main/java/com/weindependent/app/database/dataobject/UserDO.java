package com.weindependent.app.database.dataobject;

import javax.persistence.Column;

import lombok.Data;

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
    // nullable false - not public
    private Boolean isVisaPublic;
    // nullable default 0
    private Boolean notificationEnabled;
    private String avatar;
}
