package com.weindependent.app.service;

import java.util.List;

import com.weindependent.app.database.dataobject.NotificationBroadcastImageDO;
import com.weindependent.app.vo.BroadcastNotificationImgVO;

public interface IDashboardNotificationBroadcastImageService {
    Long insert(NotificationBroadcastImageDO image);
    void delete(Long id);
    List<NotificationBroadcastImageDO> listByMessageId(Long messageId);
    List<BroadcastNotificationImgVO> getTemplateStructure(String templateType);

}
