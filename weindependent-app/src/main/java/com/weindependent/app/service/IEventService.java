package com.weindependent.app.service;

import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;

import java.util.List;

public interface IEventService {
    List<RecentEventVO> getRecentEvents(int page, int size);
    EventVO getEventById(Long id);
    void register(Long id);
    void unregister(Long id);
    void bookmark(Long id);
    void unbookmark(Long id);
}
