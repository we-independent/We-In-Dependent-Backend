package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.vo.EventVO;
import com.weindependent.app.vo.RecentEventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventMapper {
    List<RecentEventVO> getRecentEvents(@Param("size") int size, @Param("offset") int offset);
    int createEvent(EventQry eventQry);
    EventVO getEventById(@Param("id") String id);
    int deleteEvent(@Param("id") Integer id);
    int updateEvent(EventDO eventDO);
}
