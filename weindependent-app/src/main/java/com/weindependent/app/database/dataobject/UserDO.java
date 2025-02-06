package com.weindependent.app.database.dataobject;

import lombok.Data;

@Data
public class UserDO {
    private Long id;
    private String username;
    private String password;
    private Integer age;
}
