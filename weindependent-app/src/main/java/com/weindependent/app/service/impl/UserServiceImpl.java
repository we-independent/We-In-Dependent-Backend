package com.weindependent.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.convertor.UserConvertor;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.service.UserService;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.utils.PasswordUtil;
import com.weindependent.app.vo.UserVO;
import org.springframework.stereotype.Service;
import com.weindependent.app.dto.RegisterQry;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

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
}
