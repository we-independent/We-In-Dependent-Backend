package com.weindependent.app.service;

import com.weindependent.app.vo.event.EventRegisterDetailVO;
import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import com.weindependent.app.vo.event.RecentEventVOs;
import com.weindependent.app.dto.EventFilterQry;

import java.util.List;

public interface IEventService {
    RecentEventVOs getUpcomingEvents(int page, int size);
    RecentEventVOs getPastEvents(int page, int size);
    List<RecentEventVO> getUpcomingByMonth(int year, int month);
    EventVO getEventById(Long id);
    EventRegisterDetailVO register(Long id);
    void unregister(Long id);
    void bookmark(Long id);
    void unbookmark(Long id);
    RecentEventVOs getRegisteredUpcomingEvents(Integer pageNum, Integer pageSize);
    RecentEventVOs getRegisteredPastEvents(Integer pageNum, Integer pageSize);
    RecentEventVOs getViewedEvents(int page, int size);
    RecentEventVOs getBookmarkedEvents(int page, int size);
    List<EventVO> searchEventsNatural(String keyword);
    List<EventVO> searchEventsBoolean(String keyword);
    RecentEventVOs filterPastEventsByTags(EventFilterQry filter);
}
