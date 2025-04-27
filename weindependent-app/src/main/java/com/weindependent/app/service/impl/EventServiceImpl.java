package com.weindependent.app.service.impl;

import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.mapper.weindependent.EventMapper;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.vo.EventVO;
import com.weindependent.app.vo.RecentEventVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EventServiceImpl implements IEventService {

    @Resource
    private EventMapper eventMapper;

    @Override
    public List<RecentEventVO> getRecentEvents(int page, int size) {
        int offset = (page - 1) * size;
        List<RecentEventVO> recentEventVOS = eventMapper.getRecentEvents(offset, size);
        if(recentEventVOS == null ){
            throw new RuntimeException("Failed to find recent events");
        }
        return eventMapper.getRecentEvents(size, offset);
    }



    @Override
    public EventVO getEventById(Integer id) {
        EventVO eventDO= eventMapper.getEventById(id);
        if (eventDO == null) {
            throw new RuntimeException("Failed to get event");
        }
        return eventDO;
    }


}