package com.weindependent.app.dto;

import java.time.LocalTime;
import lombok.Data;

@Data
public class AgendaItemQry {
    private Long itemId;
    private String title;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationMinutes;
    private Integer sortOrder;
}
