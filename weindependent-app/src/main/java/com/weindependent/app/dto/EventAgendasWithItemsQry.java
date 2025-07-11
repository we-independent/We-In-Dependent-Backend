package com.weindependent.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class EventAgendasWithItemsQry {
    private Long eventId;
    private List<AgendaWithItemsQry> agendas;
}
