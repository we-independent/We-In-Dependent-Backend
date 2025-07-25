package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class EventAgendaItemUpdateQry {
    @NotNull(message = "Item ID cannot be null")
    private Long id;
    
    @NotBlank(message = "Item title cannot be blank")
    private String title;
    
    private String description;
    
    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;
    
    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;
    
    private Integer durationMinutes;
    private Integer sortOrder;
} 