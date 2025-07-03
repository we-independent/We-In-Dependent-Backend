package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.database.dataobject.NotificationBroadcastImageDO;
import com.weindependent.app.database.dataobject.NotificationBroadcastMessageDO;
import com.weindependent.app.database.dataobject.NotificationSettingsDO;
import com.weindependent.app.database.dataobject.NotificationUserLogDO;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.database.mapper.dashboard.DashboardNotificationBroadcastMsgMapper;
import com.weindependent.app.database.mapper.weindependent.NotificationUserLogMapper;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.database.mapper.weindependent.UserNotificationMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.enums.MailTypeEnum;
import com.weindependent.app.enums.NotificationFieldEnum;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IDashboardNotificationBroadcastImageService;
import com.weindependent.app.service.IDashboardNotificationBroadcastService;
import com.weindependent.app.service.IEmailService;
import com.weindependent.app.service.IFileService;
import com.weindependent.app.service.impl.EmailServiceImpl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardNotificationBroadcastServiceImpl implements IDashboardNotificationBroadcastService{
    @Autowired 
    private DashboardNotificationBroadcastMsgMapper messageMapper;
    @Autowired 
    private UserMapper userMapper;

    @Autowired
    private UserNotificationMapper userNotificationMapper;

    @Autowired 
    private IEmailService emailService;

    @Autowired
    private IDashboardNotificationBroadcastImageService imageService;

    @Resource
    private NotificationUserLogMapper notificationUserLogMapper;

    @Override
    public void create(NotificationBroadcastMessageDO message) {
        messageMapper.insert(message);
    }

    @Override
    public void update(NotificationBroadcastMessageDO message) {
        messageMapper.update(message);
        messageMapper.markAsUnsent(message.getId());
    }

    @Override
    public void delete(Long id) {
        messageMapper.delete(id);
    }

    @Override
    public void send(Long id) {
        resend(id);
    }

    // @Override
    // public void resend(Long id) {
    //     NotificationBroadcastMessageDO msg = messageMapper.findById(id);
    //     if (msg != null) {
    //         NotificationFieldEnum fieldEnum = NotificationFieldEnum.fromMessageType(msg.getType());
    //         List<NotificationSettingsDO> settingsList = userNotificationMapper.findAll();
    //         for (NotificationSettingsDO settings : settingsList) {
    //             if (!settings.getUpdatesEnabled()) continue;
    //             if (!fieldEnum.isEnabled(settings)) continue;

    //             String email = userMapper.findById(settings.getUserId()).getEmail();
    //             log.info("user email is: ", email);
    //             MailTypeEnum mailType;
    //             if ("general".equalsIgnoreCase(msg.getType())) {
    //                 mailType = MailTypeEnum.UPDATES_GENERAL;
    //             } else if ("programs".equalsIgnoreCase(msg.getType())) {
    //                 mailType = MailTypeEnum.UPDATES_PROGRAMS;
    //             } else if ("holiday".equalsIgnoreCase(msg.getType())) {
    //                 mailType = MailTypeEnum.UPDATES_HOLIDAY;
    //             } else {
    //                 throw new ResponseException(ErrorCode.UNKNOWN_NOTIFICATION_TYPE.getCode(), "Unsupported message type: " + msg.getType());
    //             }                
    //             Map<String, String> params = new HashMap<>();
    //             params.put("title", msg.getTitle());
    //             params.put("content", msg.getContent());

    //             emailService.send(email, mailType, params);
    //         }
    //         messageMapper.markAsSent(id, LocalDateTime.now());
    //     }
    // }

@Override
public void resend(Long id) {
    NotificationBroadcastMessageDO msg = messageMapper.findById(id);
    if (msg == null) return;
    // 对应图片列表
    List<NotificationBroadcastImageDO> imageList = imageService.listByMessageId(id);

    NotificationFieldEnum fieldEnum = NotificationFieldEnum.fromMessageType(msg.getType());
    List<NotificationSettingsDO> settingsList = userNotificationMapper.findAll();

    for (NotificationSettingsDO settings : settingsList) {
        // 总开关关闭
        if (Boolean.FALSE.equals(settings.getUpdatesEnabled())) continue;
        // 当前类型通知未启用
        if (!fieldEnum.isEnabled(settings)) continue;

        // 获取用户邮箱
        UserDO user = userMapper.findById(settings.getUserId());
        if (user == null || StrUtil.isBlank(user.getAccount())) {
            log.warn("⚠️ userId={} 没有有效邮箱，跳过", settings.getUserId());
            continue;
        }

        // 选择邮件模板
        MailTypeEnum mailType;
        switch (msg.getType().toLowerCase()) {
            case "general":
                mailType = MailTypeEnum.UPDATES_GENERAL;
                break;
            case "programs":
                mailType = MailTypeEnum.UPDATES_PROGRAMS;
                break;
            case "holiday":
                mailType = MailTypeEnum.UPDATES_HOLIDAY;
                break;
            default:
                throw new ResponseException(ErrorCode.UNKNOWN_NOTIFICATION_TYPE.getCode(), "Unsupported message type: " + msg.getType());
        }

        // 构造模板参数
        Map<String, String> params = new HashMap<>();
        params.put("title", msg.getTitle());
        params.put("content", msg.getContent());
        // unsubscribeLink 可选
        params.put("unsubscribeLink", "https://weindependent.org/profile?highlight=" + fieldEnum.getFieldName());


        // 发送邮件
        log.info("发送给 userId={} email={}", user.getId(), user.getAccount());
        try {
            emailService.send(user.getAccount(), mailType, params);
            notificationUserLogMapper.insert(new NotificationUserLogDO(
                    null,                  // ID 自增
                    msg.getId(),
                    user.getId(),
                    LocalDateTime.now(),
                    "success",
                    null,
                    user.getAccount()
                ));
        } catch (Exception e) {
            emailService.send(user.getAccount(), mailType, params);
            notificationUserLogMapper.insert(new NotificationUserLogDO(
                    null,                  // ID 自增
                    msg.getId(),
                    user.getId(),
                    LocalDateTime.now(),
                    "fail",
                    e.getMessage(),
                    user.getAccount()
                ));
        }
    }

        messageMapper.markAsSent(id, LocalDateTime.now());
    }

    @Override
    public List<NotificationBroadcastMessageDO> listAll() {
        return messageMapper.findAll();
    }
}
