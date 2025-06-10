package com.weindependent.app.database.dataobject;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificationBroadcastMessageDO {
    private Long id;

    /** Notification分类: 'general', 'programs', 'holiday' */
    private String type;

    /** Notification title */
    private String title;

    /** Notification 内容 */
    private String content;

    /** 是否已发送 0-未发送，1-已发送 default 0 */
    private Boolean isSent;

    /** 最后更新时间 */
    private LocalDateTime latestSentAt;

    /** 最初创建时间 */
    private LocalDateTime  createTime;
}
