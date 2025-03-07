package com.weindependent.app.service;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.UserVO;
import com.weindependent.app.dto.UserDTO;

public interface UserService {
    UserDO queryByUsernameAndPassword(String username, String password);
    PageInfo<UserVO> getAllUsers(int pageNum, int pageSize);
    UserDTO findOrCreateUser(UserDO user);
}
