package com.weindependent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EventAgendaQry {
    @NotNull(message = "Event ID cannot be null")
    private Long eventId;
} 