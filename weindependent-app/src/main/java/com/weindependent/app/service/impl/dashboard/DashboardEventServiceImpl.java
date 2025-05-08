package com.weindependent.app.service.impl.dashboard;

import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.convertor.EventConverter;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.dataobject.EventSpeakerDO;
import com.weindependent.app.database.mapper.weindependent.EventSpeakerMapper;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.dashboard.DashboardEventImageMapper;
import com.weindependent.app.database.mapper.dashboard.DashboardEventMapper;
import com.weindependent.app.dto.EventListQry;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.FileService;
import com.weindependent.app.service.IDashboardEventService;
import com.weindependent.app.utils.ImageResizeUtil;
import com.weindependent.app.vo.UploadedFileVO;
import com.weindependent.app.vo.event.dashboard.DashboardEventVO;
import com.weindependent.app.vo.event.dashboard.DashboardEventVOs;
import com.weindependent.app.vo.user.UserVOs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.annotation.Resource;

@Service
@Slf4j
public class DashboardEventServiceImpl implements IDashboardEventService {

    @Autowired
    private FileService fileService;

    private final Integer RESIZE_WIDTH = 1729;
    private final Integer RESIZE_HEIGHT = 438;
    @Autowired
    private DashboardEventImageMapper dashboardEventImageMapper;
    @Autowired
    private DashboardEventMapper dashboardEventMapper;
    @Resource
    private EventSpeakerMapper eventSpeakerMapper;

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
        EventDO eventDO = EventConverter.toEventDO(eventQry,null,userId,userId);

        EventSpeakerDO speakerDO = new EventSpeakerDO();
        speakerDO.setUserId(eventQry.getSpeakerUserId());
        speakerDO.setTitle(eventQry.getSpeakerTitle());
        speakerDO.setBackground(eventQry.getSpeakerBackground());
        speakerDO.setDescription(eventQry.getSpeakerDescription());
        speakerDO.setBannerId(eventQry.getSpeakerBannerId());
        eventSpeakerMapper.insert(speakerDO);

        if (dashboardEventMapper.create(eventDO) == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(),"Failed to create event");
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

        EventSpeakerDO speakerDO = new EventSpeakerDO();
        speakerDO.setUserId(event.getSpeakerUserId());
        speakerDO.setTitle(event.getSpeakerTitle());
        speakerDO.setBackground(event.getSpeakerBackground());
        speakerDO.setDescription(event.getSpeakerDescription());
        speakerDO.setBannerId(event.getSpeakerBannerId());
        eventSpeakerMapper.update(speakerDO);

        if(dashboardEventMapper.update(eventDO)==0){
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Fail to update event id:"+id.toString());
        };

    }

    @Override
    public ImageDO insertEventBanner(MultipartFile file) {
        MultipartFile resizedFile;
        try {
            resizedFile = ImageResizeUtil.resizeImage(file, RESIZE_WIDTH, RESIZE_HEIGHT);
        } catch (Exception e) {
            log.error("Failed to resize image before uploading: {}", e.getMessage());
            throw new RuntimeException("Failed to resize image");
        }

        // Then upload
        UploadedFileVO uploadedFileVO = fileService.uploadFile(resizedFile, null,"event-banner" );

        ImageDO imageDo = new ImageDO();
        imageDo.setCategory("event-banner");
        imageDo.setFileName(uploadedFileVO.getFileName());
        imageDo.setFileKey(uploadedFileVO.getFileKey());
        imageDo.setFileType(resizedFile.getContentType());
        imageDo.setFilePath(uploadedFileVO.getFilePath());
        int affectedRows = dashboardEventImageMapper.insert(imageDo);
        if (affectedRows != 1) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Fail to add image to db");
        }
        return imageDo;
    }

    @Override
    public ImageDO insertSpeakerBanner(MultipartFile file) {
        MultipartFile resizedFile;
        try {
            resizedFile = ImageResizeUtil.resizeImage(file, RESIZE_WIDTH, RESIZE_HEIGHT);
        } catch (Exception e) {
            log.error("Failed to resize image before uploading: {}", e.getMessage());
            throw new RuntimeException("Failed to resize image");
        }

        UploadedFileVO uploadedFileVO = fileService.uploadFile(resizedFile, null, "event-speaker-banner");

        ImageDO imageDo = new ImageDO();
        imageDo.setCategory("event-speaker-banner");
        imageDo.setFileName(uploadedFileVO.getFileName());
        imageDo.setFileKey(uploadedFileVO.getFileKey());
        imageDo.setFileType(resizedFile.getContentType());
        imageDo.setFilePath(uploadedFileVO.getFilePath());
        int affectedRows = dashboardEventImageMapper.insert(imageDo);
        if (affectedRows != 1) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Fail to add image to db");
        }
        return imageDo;
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


}
