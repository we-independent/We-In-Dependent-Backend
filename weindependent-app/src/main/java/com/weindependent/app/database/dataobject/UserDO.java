package com.weindependent.app.database.dataobject;

import lombok.Data;

@Data
public class UserDO {
    private Long id;
    private String account;
    private String password;
    private String realName;
    private String email;
    private String loginProvider;
}
