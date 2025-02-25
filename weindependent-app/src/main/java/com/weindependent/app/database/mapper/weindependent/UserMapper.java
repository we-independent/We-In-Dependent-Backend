package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    UserDO query(Map<String, String> paramMap);
    List<UserDO> getAllUsers();
    void insertIfNotExists(UserDO user);
    UserDO findUserByEmail(String email);
}
