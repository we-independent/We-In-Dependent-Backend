package com.weindependent.app.database.dataobject;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventAgendaDO {
    private Long id;
    private Long eventId;
    private String title;
    private String description;
    private boolean isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUserId;
    private Long updateUserId;
} 