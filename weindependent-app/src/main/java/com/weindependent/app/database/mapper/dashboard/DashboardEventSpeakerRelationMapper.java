package com.weindependent.app.database.mapper.dashboard;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DashboardEventSpeakerRelationMapper {
    int insert(@Param("eventId") Long eventId, @Param("speakerId") Long speakerId);
    int deleteByEventId(@Param("eventId") Long eventId);
    List<Long> getSpeakerIdsByEventId(@Param("eventId") Long eventId);
    boolean exists(@Param("eventId") Long eventId, @Param("speakerId") Long speakerId);
}
