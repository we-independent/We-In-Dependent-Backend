package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.NotificationBroadcastMessageDO;
import com.weindependent.app.dto.NotificationPreviewRequestQry;
import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
import com.weindependent.app.enums.MailTypeEnum;
import com.weindependent.app.service.IDashboardNotificationBroadcastService;
import com.weindependent.app.service.IFileService;
import com.weindependent.app.vo.UploadedFileVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "General WI Notification 管理")
@RestController
@RequestMapping("/api/dashboard/broadcast")
public class DashboardNotificationController {
    @Autowired
    private IDashboardNotificationBroadcastService broadcastService;
    
    @Autowired
    private IFileService fileService;

    // 创建广播消息
    @SignatureAuth
    @SaCheckRole("admin")
    @PostMapping("/create")
    public NotificationBroadcastMessageDO create(@RequestBody NotificationBroadcastMessageDO message) {
        return broadcastService.create(message);
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
    
    // // 上传、更换emai中的图片
    // @SignatureAuth
    // @SaCheckRole("admin")
    // @Operation(summary = "上传邮件通知 banner 图片")
    // @PostMapping(value = "/file/notification/banner/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // public UploadedFileVO uploadNotificationEmailImg(@RequestParam("file") MultipartFile file) {
    //     return fileService.uploadFile(file, file.getOriginalFilename(), GoogleDriveFileCategoryEnum.GENERAL_NOTIFICATION);
    // }

    @SignatureAuth
    @SaCheckRole("admin")
    @PostMapping(value = "/message/render-preview", produces = "text/html;charset=UTF-8") //告诉接口返回的是 text/html 类型的内容
    @ResponseBody
    public ResponseEntity<String> render(@RequestBody NotificationPreviewRequestQry req) {
        log.info("📩 收到 preview 请求: {}", req);

        // 安全解析模板类型
        MailTypeEnum templateType;
        try {
            templateType = MailTypeEnum.valueOf(req.getType()); // 大小写一致
        } catch (IllegalArgumentException e) {
            return ResponseEntity
            .badRequest()
            .contentType(MediaType.TEXT_HTML)
            .body("<p style='color:red;'>模板类型无效: " + req.getType() + "</p>");
        }

        Map<String, String> model = new HashMap<>();
        model.put("title", req.getTitle());
        model.put("content", req.getContent());
        // 拼接按钮 HTML
        if (req.getButtonUrl() != null && !req.getButtonUrl().isEmpty()) {
            String buttonHtml = "<div style=\"text-align: center;\">" +
                    "<a href=\"" + req.getButtonUrl() + "\" class=\"button\">" +
                    req.getButtonText() + "</a></div>";
            model.put("button", buttonHtml);
        } else {
            model.put("button", ""); // 没有按钮就空
        }

        // 拼接 imageList
        StringBuilder imagesHtml = new StringBuilder();
        if (req.getImageList() != null) {
            for (NotificationPreviewRequestQry.ImageDTO image : req.getImageList()) {
                imagesHtml.append("<img src=\"")
                        .append(image.getImageUrl())
                        .append("\" alt=\"")
                        .append(image.getImageType())
                        .append("\" style=\"max-width: 100%; margin-bottom: 10px;\"/>");
            }
        }
        model.put("images", imagesHtml.toString());
        String templatePath = templateType.getPath();
        try {
            String finalHtml = broadcastService.renderTemplate(templatePath, model);
            log.info("💡 渲染结果: {}", finalHtml);
                return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(finalHtml);
        } catch (Exception e) {
            log.error("❌ 渲染失败，模板路径: {}", templatePath, e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_HTML)
                .body("<p style='color:red;'>渲染失败: 模板不存在或格式错误</p>");
        }
    }

}
