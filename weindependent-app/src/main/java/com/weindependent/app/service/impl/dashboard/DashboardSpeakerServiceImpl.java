package com.weindependent.app.service.impl.dashboard;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
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
import com.weindependent.app.service.IFileService;
import com.weindependent.app.service.IDashboardSpeakerService;
import com.weindependent.app.utils.ImageResizeUtil;
import com.weindependent.app.vo.UploadedFileVO;
import com.weindependent.app.vo.event.dashboard.DashboardSpeakerVO;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DashboardSpeakerServiceImpl implements IDashboardSpeakerService {

    private static final int RESIZE_WIDTH = 600;
    private static final int RESIZE_HEIGHT = 600;

    @Resource
    private IFileService fileService;

    @Resource
    private DashboardSpeakerMapper dashboardSpeakerMapper;

    @Resource
    private DashboardEventSpeakerImageMapper dashboardEventSpeakerImageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
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

        // Mark banner image as not deleted (set is_deleted = 0)
        int updated = dashboardEventSpeakerImageMapper.setNotDeletedById(speaker.getBannerId());
        if (updated == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Failed to activate the uploaded speaker image");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EventSpeakerQry qry) {
        // Fetch existing speaker
        EventSpeakerDO existing = dashboardSpeakerMapper.getByUserId(qry.getUserId());
        if (existing == null) {
            throw new ResponseException(ErrorCode.SPEAKER_NOT_EXIST.getCode(), "Speaker not found");
        }

        // Check if banner has changed
        Long oldBannerId = existing.getBannerId();
        Long newBannerId = qry.getBannerId();

        EventSpeakerDO speaker = new EventSpeakerDO();
        BeanUtils.copyProperties(qry, speaker);
        speaker.setId(id);
        int affected = dashboardSpeakerMapper.update(speaker);
        if (affected == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Failed to update speaker");
        }

        // If bannerId changed, mark new image active and old one deleted
        if (!newBannerId.equals(oldBannerId)) {
            dashboardEventSpeakerImageMapper.setNotDeletedById(newBannerId);
            dashboardEventSpeakerImageMapper.setDeletedById(oldBannerId);
        }
    }

    @Override
    public void delete(List<Long> ids) {
        // Get banner IDs before deleting speaker records
        List<Long> bannerIds = dashboardSpeakerMapper.getBannerIdsBySpeakerIds(ids);

        int affected = dashboardSpeakerMapper.delete(ids);
        if (affected == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Failed to delete speaker(s)");
        }

        // Loop through and mark each banner as deleted
        for (Long bannerId : bannerIds) {
            dashboardEventSpeakerImageMapper.setDeletedById(bannerId);
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
            throw new ResponseException(ErrorCode.SPEAKER_NOT_EXIST.getCode(), "Speaker not found");
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

        UploadedFileVO uploadedFileVO = fileService.uploadFile(resizedFile, null, GoogleDriveFileCategoryEnum.EVENT_SPEAKER_BANNER);

        ImageDO imageDo = new ImageDO();
        imageDo.setCategory("event-speaker-banner");
        imageDo.setFileName(uploadedFileVO.getFileName());
        imageDo.setFileKey(uploadedFileVO.getFileKey());
        imageDo.setFileType(resizedFile.getContentType());
        imageDo.setFilePath(uploadedFileVO.getFilePath());
        imageDo.setIsDeleted(1);
        int affectedRows = dashboardEventSpeakerImageMapper.insert(imageDo);
        if (affectedRows != 1) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Fail to add image to db");
        }
        return imageDo;
    }
}
