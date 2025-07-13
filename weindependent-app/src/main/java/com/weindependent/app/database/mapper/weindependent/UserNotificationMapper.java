package com.weindependent.app.database.mapper.weindependent;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.weindependent.app.database.dataobject.NotificationSettingsDO;

public interface UserNotificationMapper {
    NotificationSettingsDO findByUserId(@Param("userId") Long userId);
    int insert(NotificationSettingsDO settings);
    // int insertDefault(@Param("userId") Long userId, @Param("notificationEnabled") boolean notificationEnabled);
    int update(NotificationSettingsDO settings);
    int updateField(Map<String, Object> qry);
    List<NotificationSettingsDO> findAll(); //find notification settings for all users

} 
