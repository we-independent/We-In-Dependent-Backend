package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.dto.EventQry;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IDashboardEventService {
    void createEvent(EventQry eventQry);
    void deleteEvent(Integer id);
    void updateEvent(EventDO event);
    ImageDO insertEventBanner(MultipartFile file);
}
