package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.EventAgendaDO;
import com.weindependent.app.dto.EventAgendasWithItemsQry;
import com.weindependent.app.vo.event.EventAgendaVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventAgendaMapper {
    EventAgendaDO getByEventId(@Param("eventId") Long eventId);
    EventAgendaDO getById(@Param("id") Long id);
    List<EventAgendaDO> getAll();
    int insert(EventAgendaDO agenda);
    int update(EventAgendaDO agenda);
    int deleteById(@Param("id") Long id);
    int deleteByEventId(@Param("eventId") Long eventId);
    EventAgendasWithItemsQry getAgendasWithItemsByEventId(@Param("eventId") Long eventId);

} 