package com.weindependent.app.service.impl;

import com.weindependent.app.database.mapper.weindependent.EventMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.event.RecentEventVO;
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
        List<RecentEventVO> recentEventVOS = eventMapper.getRecent(offset, size);
        if(recentEventVOS == null ){
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(),"Cannot find events");
        }
        return eventMapper.getRecent(size, offset);
    }



    @Override
    public EventVO getEventById(Long id) {
        EventVO eventDO= eventMapper.getById(id);
        if (eventDO == null) {
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(),"Cannot find event");
        }
        return eventDO;
    }


}