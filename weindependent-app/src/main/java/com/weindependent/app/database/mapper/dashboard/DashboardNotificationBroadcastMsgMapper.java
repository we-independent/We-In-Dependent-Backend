package com.weindependent.app.database.mapper.dashboard;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.weindependent.app.database.dataobject.NotificationBroadcastMessageDO;

@Mapper
public interface DashboardNotificationBroadcastMsgMapper {
    
    int insert(NotificationBroadcastMessageDO message);

    int delete(@Param("id") Long id);

    int update(NotificationBroadcastMessageDO message);

    //查询通知广播表 notification_broadcast_message 中的所有记录
    List<NotificationBroadcastMessageDO> findAll();

    NotificationBroadcastMessageDO findById(@Param("id") Long id);

    List<NotificationBroadcastMessageDO> findUnsentMessages();

    void markAsSent(@Param("id") Long id, @Param("sentAt") LocalDateTime sentAt);

    void markAsUnsent(@Param("id") Long id);
}

