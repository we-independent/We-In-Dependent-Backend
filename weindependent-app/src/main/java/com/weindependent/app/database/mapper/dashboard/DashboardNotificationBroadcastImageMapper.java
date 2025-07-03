package com.weindependent.app.database.mapper.dashboard;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.weindependent.app.database.dataobject.NotificationBroadcastImageDO;

@Mapper
public interface DashboardNotificationBroadcastImageMapper {
    int insert(NotificationBroadcastImageDO image);
    void delete(@Param("id") Long id);
    List<NotificationBroadcastImageDO> listByMessageId(@Param("messageId") Long messageId);
}