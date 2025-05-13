package com.weindependent.app.service.impl.dashboard;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.weindependent.app.database.dataobject.EventSpeakerDO;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.mapper.dashboard.DashboardEventSpeakerImageMapper;
import com.weindependent.app.database.mapper.dashboard.DashboardSpeakerMapper;
import com.weindependent.app.dto.EventSpeakerQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.FileService;
import com.weindependent.app.service.IDashboardSpeakerService;
import com.weindependent.app.utils.ImageResizeUtil;
import com.weindependent.app.vo.UploadedFileVO;
import com.weindependent.app.vo.event.dashboard.DashboardSpeakerVO;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DashboardSpeakerServiceImpl implements IDashboardSpeakerService {

    private static final int RESIZE_WIDTH = 600;
    private static final int RESIZE_HEIGHT = 600;

    @Resource
    private FileService fileService;

    @Resource
    private DashboardSpeakerMapper dashboardSpeakerMapper;

    @Resource
    private DashboardEventSpeakerImageMapper dashboardEventSpeakerImageMapper;

    @Override
    public void create(EventSpeakerQry qry) {
        EventSpeakerDO existing = dashboardSpeakerMapper.getByUserId(qry.getUserId());
        if (existing != null) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "This user already has a speaker profile. Please update instead.");
        }

        EventSpeakerDO speaker = new EventSpeakerDO();
        BeanUtils.copyProperties(qry, speaker);
        speaker.setUserId(qry.getUserId());
        int affected = dashboardSpeakerMapper.insert(speaker);
        if (affected == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Failed to insert speaker");
        }
    }

    @Override
    public void update(Long id, EventSpeakerQry qry) {
        EventSpeakerDO speaker = new EventSpeakerDO();
        BeanUtils.copyProperties(qry, speaker);
        speaker.setId(id);
        int affected = dashboardSpeakerMapper.update(speaker);
        if (affected == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Failed to update speaker");
        }
    }

    @Override
    public void delete(List<Long> ids) {
        int affected = dashboardSpeakerMapper.delete(ids);
        if (affected == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Failed to delete speaker(s)");
        }
    }

    @Override
    public List<DashboardSpeakerVO> list() {
        List<DashboardSpeakerVO> speakers = dashboardSpeakerMapper.list();
        return speakers.stream().map(s -> {
            DashboardSpeakerVO vo = new DashboardSpeakerVO();
            BeanUtils.copyProperties(s, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public DashboardSpeakerVO getById(Long id) {
        DashboardSpeakerVO speaker = dashboardSpeakerMapper.getById(id);
        if (speaker == null) {
            throw new ResponseException(ErrorCode.EVENT_NOT_EXIST.getCode(), "Speaker not found");
        }
        DashboardSpeakerVO vo = new DashboardSpeakerVO();
        BeanUtils.copyProperties(speaker, vo);
        return vo;
    }

    @Override
    public ImageDO uploadBanner(MultipartFile file) {
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
        int affectedRows = dashboardEventSpeakerImageMapper.insert(imageDo);
        if (affectedRows != 1) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Fail to add image to db");
        }
        return imageDo;
    }
}
