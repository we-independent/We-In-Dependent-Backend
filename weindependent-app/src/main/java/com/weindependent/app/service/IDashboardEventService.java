package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.dto.EventListQry;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.vo.event.dashboard.DashboardEventVO;
import com.weindependent.app.vo.event.dashboard.DashboardEventVOs;
import com.weindependent.app.vo.event.dashboard.DashboardSpeakerVO;
import com.weindependent.app.vo.user.UserVOs;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface IDashboardEventService {
    DashboardEventVOs list(EventListQry eventListQry);
    EventDO create(EventQry eventQry);
    void delete(List<Long> ids);
    void update(Long id, EventQry eventQry);
    String insertEventBanner(MultipartFile file);
    UserVOs getRegisteredUsers(Long id, Integer page, Integer size);
    UserVOs getBookmarkedUsers(Long id, Integer page, Integer size);
    DashboardEventVO getById(Long id);
    void addSpeakersToEvent(Long eventId, List<Long> speakerIds);
    void updateSpeakersOfEvent(Long eventId, List<Long> speakerIds);
    List<DashboardSpeakerVO> getSpeakersByEventId(Long eventId);
}
