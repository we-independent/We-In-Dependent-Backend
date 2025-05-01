package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.vo.event.EventVO;
import com.weindependent.app.vo.UserVO;
import org.springframework.beans.BeanUtils;

public class EventConverter {

    public static EventDO toEventDO(EventQry eventQry, Long id, Long createUserId, Long updateUserId) {
        EventDO eventDO = new EventDO();
        BeanUtils.copyProperties(eventQry,eventDO);
        eventDO.setId(id);
        eventDO.setCreateUserId(createUserId);
        eventDO.setUpdateUserId(updateUserId);
        return eventDO;
    }
    
}
