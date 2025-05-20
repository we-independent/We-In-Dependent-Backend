package com.weindependent.app.service;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.vo.HelpCenterRequestVO;
import com.weindependent.app.vo.user.UserVO;
import com.weindependent.app.dto.HelpCenterRequestQry;
import com.weindependent.app.dto.RegisterQry;
import com.weindependent.app.dto.UpdateUserQry;
import com.weindependent.app.database.dataobject.ImageDO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {
//    UserDO queryByUsernameAndPassword(String username, String password);
    UserDO queryByEmailAndPassword(String email, String password);
    PageInfo<UserVO> getAllUsers(int pageNum, int pageSize);
    boolean registerUser(RegisterQry dto);
    UserDO findUserByAccount(String account);
    int resetPassword(String email, String newPassword);
    UserDO findUserById(Long userId); //Hurely added for pdf download
    void updateUser(UpdateUserQry updateUserQry);
    ImageDO createProfileImg(MultipartFile file);
    void saveHelpRequest(Long userId, HelpCenterRequestQry qry);
}
