package com.weindependent.app.service;

import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import com.weindependent.app.vo.event.RecentEventVOs;

import java.util.List;

public interface IEventService {
    RecentEventVOs getUpcomingEvents(int page, int size);
    RecentEventVOs getPastEvents(int page, int size);
    List<RecentEventVO> getUpcomingByMonth(int year, int month);
    EventVO getEventById(Long id);
    void register(Long id,String userTimeZone);
    void sendRegisterConfirmationEmail(Long eventId, Long userId, String userTimeZone);
    void unregister(Long id);
    void bookmark(Long id);
    void unbookmark(Long id);
    RecentEventVOs getRegisteredUpcomingEvents(Integer pageNum, Integer pageSize);
    RecentEventVOs getRegisteredPastEvents(Integer pageNum, Integer pageSize);
    RecentEventVOs getViewedEvents(int page, int size);
    RecentEventVOs getBookmarkedPastEvents(int page, int size);
    RecentEventVOs getBookmarkedUpcomingEvents(int page, int size);
    RecentEventVOs searchEventsNatural(String keyword, int pageNum, int pageSize);
    RecentEventVOs searchEventsBoolean(String keyword, int pageNum, int pageSize);
    RecentEventVOs filterPastEventsByTags(List<Integer> tagIds, int pageNum, int pageSize);
    boolean isRegistered(Long id);
}
