package com.weindependent.app.vo.event.dashboard;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DashboardEventVO {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private LocalDateTime eventTime;
    private String location;
    // private Long speakerUserId;
    // private String speakerName;
    private Long bannerId;
    private String bannerFilePath;
    private boolean isDeleted;
    private String link;
    private Integer tagId;
    private String tagName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUserId;
    private String createUserName;
    private Long updateUserId;
    private String updateUserName;
}
