package com.weindependent.app.database.dataobject;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventAgendaItemDO {
    private Long id;
    private Long agendaId;
    private String title;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationMinutes;
    private Integer sortOrder;
    private boolean isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createUserId;
    private Long updateUserId;
} 