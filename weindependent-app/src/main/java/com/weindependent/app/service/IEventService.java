package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.vo.EventVO;
import com.weindependent.app.vo.RecentEventVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IEventService {
    List<RecentEventVO> getRecentEvents(int page, int size);
    EventVO getEventById(Integer id);
}
