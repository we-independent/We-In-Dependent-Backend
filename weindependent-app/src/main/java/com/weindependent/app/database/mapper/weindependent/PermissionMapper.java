package com.weindependent.app.database.mapper.weindependent;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {
    List<String> getPermissionListByLoginId(String loginId);
}
