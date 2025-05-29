package com.weindependent.app.database.mapper.weindependent;
import org.apache.ibatis.annotations.Param;
import com.weindependent.app.database.dataobject.NotificationSettingsDO;
import com.weindependent.app.dto.NotificationFieldUpdateQry;

public interface UserNotificationMapper {
    NotificationSettingsDO findByUserId(@Param("userId") Long userId);
    int insert(NotificationSettingsDO settings);
    // int insertDefault(@Param("userId") Long userId, @Param("notificationEnabled") boolean notificationEnabled);
    int update(NotificationSettingsDO settings);
    int updateField(NotificationFieldUpdateQry qry);
} 
