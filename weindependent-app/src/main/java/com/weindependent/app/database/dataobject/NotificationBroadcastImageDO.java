package com.weindependent.app.database.dataobject;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificationBroadcastImageDO {
    private Long id;
    // 图片所属notification msg id
    private Long messageId;
    // 图片在google drive 里的url
    private String imageUrl;
    // 图片所在 tmp里的位置信息（banner，footer，etc）
    private String imageType;
    // 图片下的文字
    private String caption;
    private String buttonText;
    // 图片中所需跳转链接
    private String buttonUrl;
    // 排序信息
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
