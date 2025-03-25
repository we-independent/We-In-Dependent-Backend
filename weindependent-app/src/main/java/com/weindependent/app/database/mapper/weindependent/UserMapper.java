package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    UserDO query(Map<String, String> paramMap);
    List<UserDO> getAllUsers();
    UserDO findByAccount(String account);
    int insert(UserDO user);
}
