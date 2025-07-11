package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.NotificationSettingsDO;

public interface IUserNotificationService {
    NotificationSettingsDO getSettingsByUserId(Long userId);
    void updateNotificationField(Long userId, String fieldName, Boolean fieldValue);
    void initializeNotificationSettings(Long userId, Boolean subscribe);
}
