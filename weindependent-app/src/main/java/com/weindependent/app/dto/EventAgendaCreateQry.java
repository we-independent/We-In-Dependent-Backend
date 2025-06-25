package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EventAgendaCreateQry {
    @NotNull(message = "Event ID cannot be null")
    private Long eventId;
    
    @NotBlank(message = "Agenda title cannot be blank")
    private String title;
    
    private String description;
} 