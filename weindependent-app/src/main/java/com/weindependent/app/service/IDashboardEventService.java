package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.vo.event.dashboard.DashboardEventVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface IDashboardEventService {
    List<DashboardEventVO> getAll();
    EventDO create(EventQry eventQry);
    void delete(List<Long> ids);
    void update(Long id, EventQry eventQry);
    ImageDO insertEventBanner(MultipartFile file);
    List<UserDO> getRegisteredUsers(Long id, Integer page, Integer offset);
    List<UserDO> getBookmarkedUsers(Long id, Integer page, Integer offset);
}
