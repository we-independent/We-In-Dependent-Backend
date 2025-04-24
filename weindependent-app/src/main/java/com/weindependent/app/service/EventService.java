package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.vo.EventVO;
import com.weindependent.app.vo.RecentEventVO;

import java.util.List;

public interface EventService {
    List<RecentEventVO> getRecentEvents(int page, int size);
    void createEvent(EventQry eventQry);
    EventVO getEventById(String id);
    void deleteEvent(Integer id);
    void updateEvent(EventDO event);
}
