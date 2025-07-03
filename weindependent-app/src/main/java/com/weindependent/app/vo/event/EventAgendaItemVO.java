package com.weindependent.app.vo.event;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class EventAgendaItemVO {
    private Long id;
    private Long agendaId;
    private String title;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationMinutes;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 