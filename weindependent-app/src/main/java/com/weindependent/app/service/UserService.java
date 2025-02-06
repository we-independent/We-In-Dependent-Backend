package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.UserDO;

public interface UserService {
    UserDO queryByUsernameAndPassword(String username, String password);
}
