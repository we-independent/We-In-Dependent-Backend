package com.weindependent.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.weindependent.app.database.dataobject.NotificationBroadcastMessageDO;
import com.weindependent.app.vo.UploadedFileVO;

public interface IDashboardNotificationBroadcastService {
    void create(NotificationBroadcastMessageDO message);
    void update(NotificationBroadcastMessageDO message);
    void delete(Long id);
    void send(Long id);
    void resend(Long id);
    List<NotificationBroadcastMessageDO> listAll();
}
