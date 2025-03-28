package com.weindependent.app.service;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.GoogleUserVO;
import com.weindependent.app.vo.UserVO;
import com.weindependent.app.dto.RegisterQry;

public interface UserService {
//    UserDO queryByUsernameAndPassword(String username, String password);
    UserDO queryByEmailAndPassword(String email, String password);
    PageInfo<UserVO> getAllUsers(int pageNum, int pageSize);
    boolean registerUser(RegisterQry dto);
    GoogleUserVO findOrCreateGoogleUser(UserDO user);
    boolean resetPassword(String email, String newPassword);
}
