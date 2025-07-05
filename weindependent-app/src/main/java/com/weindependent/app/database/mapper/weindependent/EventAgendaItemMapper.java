package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.EventAgendaItemDO;
import com.weindependent.app.vo.event.EventAgendaItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventAgendaItemMapper {
    List<EventAgendaItemDO> getByAgendaId(@Param("agendaId") Long agendaId);
    EventAgendaItemDO getById(@Param("id") Long id);
    int insert(EventAgendaItemDO item);
    int update(EventAgendaItemDO item);
    int deleteById(@Param("id") Long id);
    int deleteByAgendaId(@Param("agendaId") Long agendaId);
    int updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder);
} 