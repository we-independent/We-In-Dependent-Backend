package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.EventTagDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventTagMapper {
    public List<EventTagDO> getAllTags();
}
