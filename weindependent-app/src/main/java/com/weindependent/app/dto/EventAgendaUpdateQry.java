package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EventAgendaUpdateQry {
    @NotNull(message = "Agenda ID cannot be null")
    private Long id;
    
    @NotBlank(message = "Agenda title cannot be blank")
    private String title;
    
    private String description;
} 