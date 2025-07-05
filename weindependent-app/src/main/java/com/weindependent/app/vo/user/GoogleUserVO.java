package com.weindependent.app.vo.user;

import lombok.Data;

@Data
public class GoogleUserVO {
    private Long id;
    private String email;
    private String username;
    private String language;
    private String visaType;
    private boolean subscription;
    private String avatar;
    private boolean isNewUser;
}