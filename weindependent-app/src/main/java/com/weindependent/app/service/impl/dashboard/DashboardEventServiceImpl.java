package com.weindependent.app.service.impl.dashboard;

import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.weindependent.app.convertor.EventConverter;
import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.dashboard.DashboardEventMapper;
import com.weindependent.app.database.mapper.dashboard.DashboardEventSpeakerRelationMapper;
import com.weindependent.app.database.mapper.dashboard.DashboardSpeakerMapper;
import com.weindependent.app.dto.EventListQry;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IFileService;
import com.weindependent.app.service.IDashboardEventService;
import com.weindependent.app.utils.ImageResizeUtil;
import com.weindependent.app.vo.UploadedFileVO;
import com.weindependent.app.vo.event.dashboard.DashboardEventVO;
import com.weindependent.app.vo.event.dashboard.DashboardEventVOs;
import com.weindependent.app.vo.event.dashboard.DashboardSpeakerVO;
import com.weindependent.app.vo.user.UserVOs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@Service
@Slf4j
public class DashboardEventServiceImpl implements IDashboardEventService {

    @Autowired
    private IFileService fileService;

    private final Integer RESIZE_WIDTH = 768;
    private final Integer RESIZE_HEIGHT = 422;

    @Autowired
    private DashboardEventMapper dashboardEventMapper;
    @Autowired
    private DashboardEventSpeakerRelationMapper dashboardEventSpeakerRelationMapper;
    @Autowired
    private DashboardSpeakerMapper dashboardSpeakerMapper;

    @Override
    public DashboardEventVOs list(EventListQry eventListQry) {
        Page page = PageHelper.startPage(eventListQry.getPageNum(), eventListQry.getPageSize());
        List<DashboardEventVO> events= dashboardEventMapper.list(eventListQry);
        DashboardEventVOs dashboardEventVOs = new DashboardEventVOs();
        dashboardEventVOs.setEvents(events);
        dashboardEventVOs.setPages(page.getPages());
        return dashboardEventVOs;
    }


    @Override
    public EventDO create(EventQry eventQry) {
        Long userId = StpUtil.getLoginIdAsLong();
        EventDO eventDO = EventConverter.toEventDO(eventQry, null, userId, userId);

        if (dashboardEventMapper.create(eventDO) == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Failed to create event");
        }

        return eventDO;
    }

    @Override
    public void delete(List<Long> ids) {
         Long userId = StpUtil.getLoginIdAsLong();
         if(dashboardEventMapper.delete(ids,userId)==0){
             throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(),"Failed to delete events");
         };
    }

    @Override
    public void update(Long id, EventQry event) {
        Long userId = StpUtil.getLoginIdAsLong();
        EventDO eventDO = EventConverter.toEventDO(event,id,null,userId);

        if(dashboardEventMapper.update(eventDO)==0){
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Fail to update event id:"+id.toString());
        };


    }

    @Override
    public String insertEventBanner(MultipartFile file) {
        MultipartFile resizedFile;
        try {
            resizedFile = ImageResizeUtil.resizeImage(file, RESIZE_WIDTH, RESIZE_HEIGHT);
        } catch (Exception e) {
            log.error("Failed to resize image before uploading: {}", e.getMessage());
            throw new RuntimeException("Failed to resize image");
        }

        UploadedFileVO uploadedFileVO = fileService.uploadFile(resizedFile, null, GoogleDriveFileCategoryEnum.EVENT_BANNER);
        return uploadedFileVO.getFilePath();
    }

    @Override
    public UserVOs getRegisteredUsers(Long id, Integer page, Integer size) {
        Page p = PageHelper.startPage(page,size);
        List<UserDO> userDOs = dashboardEventMapper.getRegisteredUsers(id);
        UserVOs userVOs = new UserVOs();
        userVOs.setUsers(userDOs);
        userVOs.setPages(p.getPages());
        return userVOs;
    }

    @Override
    public UserVOs getBookmarkedUsers(Long id, Integer page, Integer size) {
        Page p = PageHelper.startPage(page,size);
        List<UserDO> userDOs =  dashboardEventMapper.getBookmarkedUsers(id);
        UserVOs userVOs = new UserVOs();
        userVOs.setUsers(userDOs);
        userVOs.setPages(p.getPages());
        return userVOs;
    }

    @Override
    public DashboardEventVO getById(Long id) {
        return dashboardEventMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSpeakersToEvent(Long eventId, List<Long> speakerIds) {
        for (Long speakerId : speakerIds) {
            boolean exists = dashboardEventSpeakerRelationMapper.exists(eventId, speakerId);
            if (!exists) {
                dashboardEventSpeakerRelationMapper.insert(eventId, speakerId);
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpeakersOfEvent(Long eventId, List<Long> speakerIds) {
        dashboardEventSpeakerRelationMapper.deleteByEventId(eventId);
        addSpeakersToEvent(eventId, speakerIds);
    }


    @Override
    public List<DashboardSpeakerVO> getSpeakersByEventId(Long eventId) {
        List<Long> speakerIds = dashboardEventSpeakerRelationMapper.getSpeakerIdsByEventId(eventId);
        if (speakerIds == null || speakerIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return dashboardSpeakerMapper.getByIds(speakerIds);
    }

}
