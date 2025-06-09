package com.weindependent.app.vo.user;

import lombok.Data;

@Data
public class UserVO {
    private String username; //this is same as account from registerQry, ie email
    private String name; //this is same as realName from registerQry
    private String language;
    private String visaType;
    private boolean subscription;
}
