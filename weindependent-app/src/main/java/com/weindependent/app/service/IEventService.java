package com.weindependent.app.service;

import com.weindependent.app.vo.event.EventRegisterDetailVO;
import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import com.weindependent.app.vo.event.RecentEventVOs;

import java.util.List;

public interface IEventService {
    List<RecentEventVO> getRecentEvents(int page, int size);
    EventVO getEventById(Long id);
    EventRegisterDetailVO register(Long id);
    void unregister(Long id);
    void bookmark(Long id);
    void unbookmark(Long id);
    RecentEventVOs getRegisteredOngoingEvents(Integer pageNum, Integer pageSize);
    RecentEventVOs getRegisteredPastEvents(Integer pageNum, Integer pageSize);
    RecentEventVOs getViewedEvents(int page, int size);
    RecentEventVOs getBookmarkedEvents(int page, int size);
}
