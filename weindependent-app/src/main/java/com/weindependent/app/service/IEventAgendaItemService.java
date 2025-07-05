package com.weindependent.app.service;

import com.weindependent.app.dto.EventAgendaItemCreateQry;
import com.weindependent.app.dto.EventAgendaItemUpdateQry;
import com.weindependent.app.vo.event.EventAgendaItemVO;

import java.util.List;

public interface IEventAgendaItemService {
    List<EventAgendaItemVO> getItemsByAgendaId(Long agendaId);
    EventAgendaItemVO getItemById(Long id);
    EventAgendaItemVO createItem(EventAgendaItemCreateQry qry);
    EventAgendaItemVO updateItem(EventAgendaItemUpdateQry qry);
    void deleteItem(Long id);
    void deleteItemsByAgendaId(Long agendaId);
    void updateItemSortOrder(Long id, Integer sortOrder);
} 