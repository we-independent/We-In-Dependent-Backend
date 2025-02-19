package com.weindependent.app.service.impl;

import com.weindependent.app.convertor.UserConvertor;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.service.UserService;
import com.weindependent.app.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDO queryByUsernameAndPassword(String username, String password) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("password", password);
        return userMapper.query(paramMap);
    }

    @Override
    public List<UserVO> getAllUsers() {
        return UserConvertor.toUserVOListEntity(userMapper.getAllUsers());
    }
}
