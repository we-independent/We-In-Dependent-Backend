package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.vo.event.EventSpeakerVO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EventSpeakerMapper {
    EventSpeakerVO getById(@Param("id") Long id);
    List<EventSpeakerVO> searchByName(@Param("name") String name);
    List<EventSpeakerVO> getSpeakerByEventId(@Param("eventId") Long eventId);
}