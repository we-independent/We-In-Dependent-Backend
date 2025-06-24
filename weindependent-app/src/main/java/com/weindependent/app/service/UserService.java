package com.weindependent.app.service;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.dto.ChangePasswordQry;
import com.weindependent.app.dto.HelpCenterRequestQry;
import com.weindependent.app.dto.VerifyPasswordQry;
import com.weindependent.app.vo.user.UserVO;
import com.weindependent.app.dto.RegisterQry;
import com.weindependent.app.dto.UpdateUserQry;
import com.weindependent.app.database.dataobject.ImageDO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

public interface UserService {
//    UserDO queryByUsernameAndPassword(String username, String password);
    UserDO queryByEmailAndPassword(String email, String password);
    PageInfo<UserVO> getAllUsers(int pageNum, int pageSize);
    boolean registerUser(RegisterQry dto);
    UserDO findUserByAccount(String account);
    int resetPassword(String email, String newPassword);
    UserDO findUserById(Long userId); //Hurely added for pdf download
    void updateUser(UpdateUserQry updateUserQry);
    String createProfileImg(MultipartFile file);
    void verifyPassword(@Valid VerifyPasswordQry verifyPasswordQry);
    void changePassword(ChangePasswordQry changePasswordQry);
    void deleteAccount(Long userId);
    void saveHelpRequest(Long userId, HelpCenterRequestQry qry);

}
