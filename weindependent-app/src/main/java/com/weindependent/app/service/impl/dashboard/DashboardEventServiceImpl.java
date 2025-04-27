package com.weindependent.app.service.impl.dashboard;

import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.dataobject.EventDO;
import com.weindependent.app.database.mapper.dashboard.DashboardEventImageMapper;
import com.weindependent.app.database.mapper.weindependent.EventMapper;
import com.weindependent.app.dto.EventQry;
import com.weindependent.app.service.FileService;
import com.weindependent.app.service.IDashboardEventService;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.utils.ImageResizeUtil;
import com.weindependent.app.vo.EventVO;
import com.weindependent.app.vo.UploadedFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
@Slf4j
public class DashboardEventServiceImpl implements IDashboardEventService {

    @Resource
    private EventMapper eventMapper;
    @Autowired
    private FileService fileService;

    private final Integer RESIZE_WIDTH = 1729;
    private final Integer RESIZE_HEIGHT = 438;
    @Autowired
    private DashboardEventImageMapper dashboardEventImageMapper;


    @Override
    public void createEvent(EventQry eventQry) {
        if (eventMapper.createEvent(eventQry) == 0) {
            throw new RuntimeException("Failed to create event");
        }
    }

    @Override
    public void deleteEvent(Integer id) {
        int rows = eventMapper.deleteEventBanner(id);
        if (rows == 0) {
            log.warn("No events banner found");
        }
        rows = eventMapper.deleteEvent(id);
        if (rows == 0) {
            throw new RuntimeException("Delete failed");
        }
    }

    @Override
    public void updateEvent(EventDO event) {
        if (eventMapper.updateEvent(event) == 0) {
            throw new RuntimeException("Failed to update event with id: " + event.getId());
        }
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
