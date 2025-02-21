package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.UserVO;

import java.util.List;

public interface UserService {
    UserDO queryByUsernameAndPassword(String username, String password);
    List<UserVO> getAllUsers();
}
