package com.weindependent.app.service;

import com.weindependent.app.controller.EventSpeakerController;
import com.weindependent.app.vo.event.EventRegisterDetailVO;
import com.weindependent.app.vo.event.EventSpeakerVO;
import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
import com.weindependent.app.vo.event.RecentEventVOs;
import org.springframework.scheduling.annotation.Async;

import java.time.ZoneId;
import java.util.List;

public interface IEventService {
    RecentEventVOs getUpcomingEvents(int page, int size);
    RecentEventVOs getPast(int page, int size, String keyword, List<Integer> filter);
    List<RecentEventVO> getUpcomingByMonth(int year, int month);
    EventVO getEventById(Long id);
    EventRegisterDetailVO register(Long id, ZoneId zoneId);
    @Async
    void sendRegisterConfirmationEmail( Long userId, ZoneId zoneId, EventVO event, String googleCalendarLink);
    void unregister(Long id);
    void bookmark(Long id);
    void unbookmark(Long id);
    RecentEventVOs getRegisteredUpcomingEvents(Integer pageNum, Integer pageSize);
    RecentEventVOs getRegisteredPastEvents(Integer pageNum, Integer pageSize);
    RecentEventVOs getViewedEvents(int page, int size);
    RecentEventVOs getBookmarkedPastEvents(int page, int size);
    RecentEventVOs getBookmarkedUpcomingEvents(int page, int size);
    boolean isRegistered(Long id);
    String generateGoogleCalendarLink(EventVO eventVO, ZoneId zoneId);
    ZoneId getZoneIdByUserTimeZone(String userTimeZone);
   
}
