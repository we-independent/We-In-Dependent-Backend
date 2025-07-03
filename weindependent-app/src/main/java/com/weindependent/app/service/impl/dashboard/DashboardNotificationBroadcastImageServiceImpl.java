package com.weindependent.app.service.impl.dashboard;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.weindependent.app.exception.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.database.dataobject.NotificationBroadcastImageDO;
import com.weindependent.app.database.mapper.dashboard.DashboardNotificationBroadcastImageMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.enums.NotificationTemplateImageConfigEnum;
import com.weindependent.app.service.IDashboardNotificationBroadcastImageService;
import com.weindependent.app.vo.BroadcastNotificationImgVO;

@Service
public class DashboardNotificationBroadcastImageServiceImpl implements IDashboardNotificationBroadcastImageService {

    @Autowired
    private DashboardNotificationBroadcastImageMapper imageMapper;

    @Override
    public Long insert(NotificationBroadcastImageDO image) {
        imageMapper.insert(image);
        return image.getId();
    }

    @Override
    public void delete(Long id) {
        imageMapper.delete(id);
    }

    @Override
    public List<NotificationBroadcastImageDO> listByMessageId(Long messageId) {
        return imageMapper.listByMessageId(messageId);
    }

    @Override
    public List<BroadcastNotificationImgVO> getTemplateStructure(String templateType){
        return Arrays.stream(NotificationTemplateImageConfigEnum.values())
                     .filter(e -> e.getTemplateType().equalsIgnoreCase(templateType))
                     .findFirst()
                     .orElseThrow(() -> new ResponseException(ErrorCode.INVALID_PARAM.getCode(), "通知类型不存在" + templateType))
                     .getImageSlots()
                     .stream()
                     .map(slot -> {
                        BroadcastNotificationImgVO vo = new BroadcastNotificationImgVO();
                        vo.setImgType(slot.getImgType());
                        vo.setPosition(slot.getPosition());
                        vo.setCount(slot.getCount());
                        return vo;
                     })
                     .collect(Collectors.toList());
    }
}

