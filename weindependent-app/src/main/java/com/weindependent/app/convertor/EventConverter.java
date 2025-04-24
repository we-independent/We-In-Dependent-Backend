package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.vo.EventVO;
import com.weindependent.app.vo.UserVO;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class EventConverter {

    public static EventVO toEventVO(EventDO eventDO, UserVO speaker) {
        EventVO eventVO = new EventVO();
        BeanUtils.copyProperties(eventDO,eventVO);
        eventVO.setSpeaker(speaker);
        return eventVO;
    }
    
}
