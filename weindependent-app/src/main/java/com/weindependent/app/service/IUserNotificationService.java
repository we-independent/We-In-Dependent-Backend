package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.NotificationSettingsDO;
import com.weindependent.app.enums.NotificationFieldEnum;

public interface IUserNotificationService {
    NotificationSettingsDO getSettingsByUserId(Long userId);
    void updateNotificationField(Long userId, NotificationFieldEnum field, Boolean fieldValue);
    void initializeNotificationSettings(Long userId, Boolean subscribe);
}
