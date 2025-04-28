package com.weindependent.app.service.impl.dashboard;

import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.convertor.EventConverter;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.mapper.dashboard.DashboardEventImageMapper;
import com.weindependent.app.database.mapper.dashboard.DashboardEventMapper;
import com.weindependent.app.database.mapper.weindependent.EventMapper;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.service.FileService;
import com.weindependent.app.service.IDashboardEventService;
import com.weindependent.app.utils.ImageResizeUtil;
import com.weindependent.app.vo.UploadedFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

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
    @Autowired
    private EventMapper eventMapper;


    @Override
    public EventDO create(EventQry eventQry) {
        int userId = StpUtil.getLoginIdAsInt();
        EventDO eventDO = EventConverter.toEventDO(eventQry,null,userId,null);
        if (dashboardEventMapper.create(eventDO) == 0) {
            throw new RuntimeException("Failed to create event");
        }
        return eventDO;
    }

    @Override
    public void delete(List<Integer> ids) {
         int userId = StpUtil.getLoginIdAsInt();
         dashboardEventImageMapper.delete(ids);
         dashboardEventMapper.delete(ids,userId);
    }

    @Override
    public void update(Integer id, EventQry event) {
        int userId = StpUtil.getLoginIdAsInt();
        EventDO eventDO = EventConverter.toEventDO(event,id,null,userId);
        dashboardEventMapper.update(eventDO);

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
            throw new RuntimeException("Failed to add image to database");
        }
        return imageDo;
    }

}
