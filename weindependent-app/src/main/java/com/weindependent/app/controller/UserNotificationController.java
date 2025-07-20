package com.weindependent.app.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.database.dataobject.NotificationSettingsDO;
import com.weindependent.app.dto.UpdateNotificationFieldQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.enums.NotificationFieldEnum;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.impl.UserNotificationServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/api/user/notifications")
public class UserNotificationController {
    @Resource
    private UserNotificationServiceImpl notificationService;

    @GetMapping("/settings")
    public NotificationSettingsDO getNotificationSettings() {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("正在获取通知设置 userId = {}", userId);
        return notificationService.getSettingsByUserId(userId);
    }

    @SaCheckLogin
    @PostMapping(value = "/settings/update", consumes = "application/json;charset=UTF-8")
    public void updateField(@RequestBody UpdateNotificationFieldQry notificationQry) {
        Long userId = StpUtil.getLoginIdAsLong();
        NotificationFieldEnum field = notificationQry.getField();
        notificationService.updateNotificationField(userId, field, notificationQry.getFieldValue());
    }
}
