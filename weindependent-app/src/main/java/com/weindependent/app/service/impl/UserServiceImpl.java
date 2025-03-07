package com.weindependent.app.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.convertor.UserConvertor;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.service.UserService;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.vo.UserVO;
import com.weindependent.app.dto.UserDTO;
import com.weindependent.app.convertor.UserConvertor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserConvertor userConvertor;

    @Override
    public UserDO queryByUsernameAndPassword(String username, String password) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("password", password);
        return userMapper.query(paramMap);
    }

    @Override
    public PageInfo<UserVO> getAllUsers(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserDO> userDOList = userMapper.getAllUsers();
        PageInfo<UserDO> userDOPageInfo = new PageInfo<>(userDOList);
        return PageInfoUtil.pageInfo2DTO(userDOPageInfo, UserVO.class);
    }

    @Override
    public UserDTO findOrCreateUser(UserDO user) {
        UserDO foundUser= userMapper.findUserByEmail(user.getEmail());
        boolean isNewUser=false;
        if(foundUser==null){
            isNewUser=true;
            userMapper.insertUser(user);
            foundUser=user;
        }
        return userConvertor.toUserDTOEntity(foundUser,isNewUser);
    }
}
