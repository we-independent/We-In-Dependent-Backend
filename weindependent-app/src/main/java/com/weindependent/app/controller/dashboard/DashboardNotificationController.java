package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.NotificationBroadcastMessageDO;
import com.weindependent.app.service.IDashboardNotificationBroadcastService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "General WI Notification 管理")
@RestController
@RequestMapping("/api/dashboard/broadcast")
public class DashboardNotificationController {
    @Autowired
    private IDashboardNotificationBroadcastService broadcastService;

    // 创建广播消息
    @SignatureAuth
    @SaCheckRole("admin")
    @PostMapping("/create")
    public void create(@RequestBody NotificationBroadcastMessageDO message) {
        broadcastService.create(message);
    }

    // 更新广播消息（更新后自动置 is_sent = false）
    @SignatureAuth
    @SaCheckRole("admin")
    @PutMapping("/update")
    public void update(@RequestBody NotificationBroadcastMessageDO message) {
        broadcastService.update(message);
    }

    // 删除指定广播消息
    @SignatureAuth
    @SaCheckRole("admin")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        broadcastService.delete(id);
    }

    //初次发送某条广播（根据用户设置判断是否发送）
    @SignatureAuth
    @SaCheckRole("admin")
    @PostMapping("/send/{id}")
    public void send(@PathVariable Long id) {
        broadcastService.send(id);
    }

    // 重新发送某条广播（根据用户设置判断是否发送）
    @SignatureAuth
    @SaCheckRole("admin")
    @PostMapping("/resend/{id}")
    public void resend(@PathVariable Long id) {
        broadcastService.resend(id);
    }

    // 查询所有广播消息（用于 dashboard 表格列表）
    @SignatureAuth
    @SaCheckRole("admin")
    @GetMapping("/list")
    public List<NotificationBroadcastMessageDO> listAll() {
        return broadcastService.listAll();
    }
    
}
