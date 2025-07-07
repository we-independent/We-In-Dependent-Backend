package com.weindependent.app.controller.dashboard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.weindependent.app.database.dataobject.NotificationBroadcastImageDO;
import com.weindependent.app.database.dataobject.NotificationBroadcastMessageDO;
import com.weindependent.app.database.mapper.dashboard.DashboardNotificationBroadcastMsgMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
import com.weindependent.app.enums.MailTypeEnum;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IDashboardNotificationBroadcastImageService;
import com.weindependent.app.service.IFileService;
import com.weindependent.app.vo.BroadcastNotificationImgVO;
import com.weindependent.app.vo.UploadedFileVO;

import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.core.io.Resource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.weindependent.app.annotation.SignatureAuth;

@RestController
@SaCheckRole("admin")
@RequestMapping("/api/dashboard/broadcast")
@Tag(name = "后台通知图管理")
public class DashboardNotificationImageController {

    @Autowired
    private IDashboardNotificationBroadcastImageService imageService;
    @Autowired
    private IFileService fileService;
    @Autowired
    private DashboardNotificationBroadcastMsgMapper messageMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Operation(summary = "上传图")
    @SignatureAuth
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long upload(@RequestParam("file") MultipartFile file,
                       @RequestParam("messageId") Long messageId,
                       @RequestParam("imageType") String imageType,
                       @RequestParam(value = "caption", required = false) String caption,
                       @RequestParam(value = "buttonText", required = false) String buttonText,
                       @RequestParam(value = "buttonUrl", required = false) String buttonUrl,
                       @RequestParam(value = "sortOrder", required = false) Integer sortOrder) {
        System.out.println("✅ 收到图片上传 messageId = " + messageId);

        UploadedFileVO uploaded = fileService.uploadFile(file, file.getOriginalFilename(), GoogleDriveFileCategoryEnum.GENERAL_NOTIFICATION);
        NotificationBroadcastMessageDO msg = messageMapper.findById(messageId);
        if (msg == null) {
            throw new ResponseException(ErrorCode.MESSAGE_NOT_EXIST.getCode(), "messageId 无效");
        }
        NotificationBroadcastImageDO img = new NotificationBroadcastImageDO();
        img.setMessageId(messageId);
        img.setImageType(imageType);
        img.setImageUrl(uploaded.getFilePath());
        img.setCaption(caption);
        img.setButtonText(buttonText);
        img.setButtonUrl(buttonUrl);
        if (sortOrder == null) {
            sortOrder = 1;
        }
        img.setSortOrder(sortOrder);

        return imageService.insert(img);    
}

    @Operation(summary = "删除图")
    @DeleteMapping("/image/{id}")
    @SignatureAuth
    public void delete(@PathVariable Long id) {
        imageService.delete(id);
    }

    @Operation(summary = "查询指定通知的图")
    @GetMapping("/image/list/{messageId}")
    @SignatureAuth
    public List<NotificationBroadcastImageDO> list(@PathVariable Long messageId) {
        return imageService.listByMessageId(messageId);
    }

    @SignatureAuth
    @Operation(summary = "获取指定模板类型的图片上传配置")
    @GetMapping("/image/template/{type}")
    public List<BroadcastNotificationImgVO> getTemplateImageStructure(@PathVariable String type) {
        return imageService.getTemplateStructure(type);

    }

    @SignatureAuth
    @Operation(summary = "获取预览的email template")
    @GetMapping("/email/template/{type}")
    public String getEmailTemplate(@PathVariable String type) {
        MailTypeEnum mailType = MailTypeEnum.valueOf(type);

        try (InputStream inputStream = resourceLoader.getResource(mailType.getPath()).getInputStream()) {
            String html = readInputStream(inputStream);
            return html; // 不经过 ResponseResult 包装
        } catch (IOException e) {
                throw new ResponseException(ErrorCode.INVALID_PARAM.getCode(), "加载模板失败：" + e.getMessage());
            }
        }

        public static String readInputStream(InputStream inputStream) throws IOException {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        }
}
