package com.weindependent.app.service.impl;

import com.weindependent.app.database.dataobject.NotificationSettingsDO;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.database.mapper.weindependent.UserNotificationMapper;
import com.weindependent.app.dto.NotificationFieldUpdateQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.enums.NotificationFieldEnum;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IUserNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserNotificationServiceImpl implements IUserNotificationService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserNotificationMapper userNotificationMapper;

    @Override
    public NotificationSettingsDO getSettingsByUserId(Long userId) {
        NotificationSettingsDO notificationDO = userNotificationMapper.findByUserId(userId);
        if (notificationDO == null){
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), "Invalid user: " + userId);
        }
        return notificationDO;
    }

    @Override
    public void updateNotificationField(Long userId, NotificationFieldEnum field, Boolean fieldValue) {

        NotificationSettingsDO notificationDO = userNotificationMapper.findByUserId(userId);
        if (notificationDO == null) {
            Boolean notificationEnable = userMapper.findNotificationEnabledByUserId(userId);
            initializeNotificationSettings(userId, Boolean.TRUE.equals(notificationEnable));
        } else {
            NotificationFieldUpdateQry qry = new NotificationFieldUpdateQry();
            qry.setUserId(userId);
            qry.setFieldName(field.getFieldName());
            qry.setFieldValue(fieldValue);
            userNotificationMapper.updateField(qry);
        }
    }

    @Override
    public void initializeNotificationSettings(Long userId, Boolean subscribe) {
        NotificationSettingsDO settingsDO = new NotificationSettingsDO();
        settingsDO.setUserId(userId);
        if (!Boolean.TRUE.equals(subscribe)) {
            settingsDO.setUpdatesEnabled(false);
            settingsDO.setUpdatesGeneralAnnouncements(false);
            settingsDO.setUpdatesNewProgramsOrFeatures(false);
            settingsDO.setUpdatesHolidayMessages(false);
        }
        userNotificationMapper.insert(settingsDO);
    }
}
