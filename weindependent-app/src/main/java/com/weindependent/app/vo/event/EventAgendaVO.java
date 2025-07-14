package com.weindependent.app.vo.event;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventAgendaVO {
    private Long id;
    private Long eventId;
    private String title;
    private String description;
    private Instant eventStartTime;
    private Integer eventDuration;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<EventAgendaItemVO> items;
} 