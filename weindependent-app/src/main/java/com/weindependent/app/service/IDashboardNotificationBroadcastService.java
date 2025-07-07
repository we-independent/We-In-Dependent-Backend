package com.weindependent.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.weindependent.app.database.dataobject.NotificationBroadcastMessageDO;
import com.weindependent.app.dto.NotificationPreviewRequestQry;
import com.weindependent.app.vo.UploadedFileVO;

public interface IDashboardNotificationBroadcastService {
    NotificationBroadcastMessageDO create(NotificationBroadcastMessageDO message);
    void update(NotificationBroadcastMessageDO message);
    void delete(Long id);
    void send(Long id);
    void resend(Long id);
    List<NotificationBroadcastMessageDO> listAll();
    String renderTemplate(String templatePath, Map<String, String> params);
}