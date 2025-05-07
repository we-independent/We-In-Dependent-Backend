package com.weindependent.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.service.UserService;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.utils.PasswordUtil;
import com.weindependent.app.vo.user.UserVO;

import cn.dev33.satoken.temp.SaTempUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.weindependent.app.dto.RegisterQry;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private ResetUserPasswordEmailServiceImpl resetUserPasswordEmailService;

//    @Override
//    public UserDO queryByUsernameAndPassword(String username, String password) {
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("username", username);
//        paramMap.put("password", password);
//        return userMapper.query(paramMap);
//    }

//    @Override
//    public UserDO queryByEmailAndPassword(String email, String password) {
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("email", email);
//        paramMap.put("password", password);
//        return userMapper.query(paramMap);
//    }

    @Override
    public UserDO queryByEmailAndPassword(String email, String password) {
        UserDO user = userMapper.findByAccount(email);
        if (user == null || !PasswordUtil.verifyPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }

    @Override
    public PageInfo<UserVO> getAllUsers(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserDO> userDOList = userMapper.getAllUsers();
        PageInfo<UserDO> userDOPageInfo = new PageInfo<>(userDOList);
        return PageInfoUtil.pageInfo2DTO(userDOPageInfo, UserVO.class);
    }

    @Override
    public boolean registerUser(RegisterQry dto) {
        UserDO existingUser = userMapper.findByAccount(dto.getAccount());
        if (existingUser != null) {
            throw new RuntimeException("Email already registered");
        }

        String hashedPassword = PasswordUtil.hashPassword(dto.getPassword());

        UserDO user = new UserDO();
        user.setAccount(dto.getAccount());
        user.setPassword(hashedPassword);
        user.setRealName(dto.getRealName());
        user.setLanguage(dto.getLanguage());
        user.setVisaType(dto.getVisaType());
        user.setLoginProvider("local");
        user.setSubscription(dto.isSubscription());

        return userMapper.insert(user) > 0;
    }

    @Override
    public UserDO findUserByAccount(String account) {
        return userMapper.findByAccount(account);
    }

    @Override
    public int resetPassword(String token, String newPassword){
        //temp token see reference https://sa-token.cc/doc.html#/plugin/temp-token?id=临时token令牌认证
        String userId = SaTempUtil.parseToken(token, String.class);

        long timeout = SaTempUtil.getTimeout(token);
        if (timeout < 0){
            log.info("Reset password failed because token does not exist for userId: {}.", userId);
            return ErrorCode.TOKEN_NOT_EXIST_OR_EXPIRED.getCode(); //Token does not exist or expired.
        }

        UserDO user = userMapper.findById(userId);
        if (user == null) {
            log.info("Reset password failed because user does not exist for userId: {}.", userId);
            return ErrorCode.USER_NOT_EXIST.getCode(); //user does not exist
        }

        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        user.setPassword(hashedPassword);
        if (userMapper.updatePassword(Integer.parseInt(userId), hashedPassword) <= 0) {
            log.info("Reset password failed because cannot update in DB for userId: {}.", userId);
            return ErrorCode.UPDATE_DB_FAILED.getCode(); //database update failed
        }

        SaTempUtil.deleteToken(token);
        log.info("Reset password successful for userId: {}.", userId);
        return 0; //success
    }

    //Hurely add for pdf download
    @Override
    public UserDO findUserById(Long userId) {
        return userMapper.findById(userId); 
    }
}

