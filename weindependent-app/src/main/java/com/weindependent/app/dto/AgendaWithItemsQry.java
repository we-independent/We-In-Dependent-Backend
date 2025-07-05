package com.weindependent.app.dto;

import java.util.List;
import lombok.Data;

@Data
public class AgendaWithItemsQry {
    private Long agendaId;
    private String title;
    private String description;
    private List<AgendaItemQry> items;
}
