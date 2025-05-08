package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.EventSpeakerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EventSpeakerMapper {
    int insert(EventSpeakerDO speaker);
    int update(EventSpeakerDO speaker);
    EventSpeakerDO getByUserId(@Param("userId") Long userId);
    EventSpeakerDO getById(@Param("id") Long id);
}