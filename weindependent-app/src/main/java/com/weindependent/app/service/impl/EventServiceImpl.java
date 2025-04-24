package com.weindependent.app.service.impl;

import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.mapper.weindependent.EventMapper;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.service.EventService;
import com.weindependent.app.vo.EventVO;
import com.weindependent.app.vo.RecentEventVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Resource
    private EventMapper eventMapper;

    @Override
    public List<RecentEventVO> getRecentEvents(int page, int size) {
        int offset = (page - 1) * size;
        return eventMapper.getRecentEvents(size, offset);
    }

    @Override
public void createEvent(EventQry eventQry) {
        if (eventMapper.createEvent(eventQry) == 0) {
            throw new RuntimeException("Failed to create event");
        }
    }

    @Override
    public EventVO getEventById(String id) {
        return eventMapper.getEventById(id);
    }

    @Override
    public void deleteEvent(Integer id) {
        if (eventMapper.deleteEvent(id) == 0) {
            throw new RuntimeException("Failed to delete event with id: " + id);
        }
    }

    @Override
    public void updateEvent(EventDO event) {
        if (eventMapper.updateEvent(event) == 0) {
            throw new RuntimeException("Failed to update event with id: " + event.getId());
        }
    }
}