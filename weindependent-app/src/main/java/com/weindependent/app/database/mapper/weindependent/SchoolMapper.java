package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.SchoolDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SchoolMapper {
    List<SchoolDO> getNearbySchools(Map<String, Object> paramMap);
}
