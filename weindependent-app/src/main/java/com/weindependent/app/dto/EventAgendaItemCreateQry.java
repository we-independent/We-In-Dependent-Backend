package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

@Data
public class EventAgendaItemCreateQry {
    @NotNull(message = "Agenda ID cannot be null")
    private Long agendaId;
    
    @NotBlank(message = "Item title cannot be blank")
    private String title;
    
    private String description;
    
    @NotNull(message = "Start time cannot be null")
    @Schema(description = "Start time of the agenda item", example = "08:30:00")
    @JsonFormat(pattern = "HH:mm:ss") //后端接受的时间格式
    private LocalTime startTime;
    
    @NotNull(message = "End time cannot be null")
    @Schema(description = "End time of the agenda item", example = "08:30:00")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    
    private Integer durationMinutes;
    private Integer sortOrder;
} 