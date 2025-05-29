package com.weindependent.app.database.mapper.weindependent;
import org.apache.ibatis.annotations.Param;
import com.weindependent.app.database.dataobject.NotificationSettingsDO;

public interface UserNotificationMapper {
    NotificationSettingsDO findByUserId(@Param("userId") Long userId);
    int insert(NotificationSettingsDO settings);
    int insertDefault(Long userId);
    int update(NotificationSettingsDO settings);
    int updateField(@Param("userId") Long userId,
                @Param("fieldName") String fieldName,
                @Param("fieldValue") Boolean fieldValue);
} 
