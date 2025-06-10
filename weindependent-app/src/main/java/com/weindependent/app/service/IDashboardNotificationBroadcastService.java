package com.weindependent.app.service;

import java.util.List;

import com.weindependent.app.database.dataobject.NotificationBroadcastMessageDO;

public interface IDashboardNotificationBroadcastService {
    void create(NotificationBroadcastMessageDO message);
    void update(NotificationBroadcastMessageDO message);
    void delete(Long id);
    void send(Long id);
    void resend(Long id);
    List<NotificationBroadcastMessageDO> listAll();
}
