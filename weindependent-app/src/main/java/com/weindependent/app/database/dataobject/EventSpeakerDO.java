package com.weindependent.app.database.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventSpeakerDO {
    private Long id;
    private Long userId;
    private String title;
    private String background;
    private String description;
    private Long bannerId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}