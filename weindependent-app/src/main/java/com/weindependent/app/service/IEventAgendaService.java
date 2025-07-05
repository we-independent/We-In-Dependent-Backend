package com.weindependent.app.service;

import com.weindependent.app.dto.EventAgendaCreateQry;
import com.weindependent.app.dto.EventAgendaUpdateQry;
import com.weindependent.app.dto.EventAgendasWithItemsQry;
import com.weindependent.app.vo.event.EventAgendaVO;

public interface IEventAgendaService {
    EventAgendaVO getAgendaByEventId(Long eventId);
    EventAgendaVO createAgenda(EventAgendaCreateQry qry);
    EventAgendaVO updateAgenda(EventAgendaUpdateQry qry);
    void deleteAgenda(Long id);
    void deleteAgendaByEventId(Long eventId);
    EventAgendasWithItemsQry getAgendasWithItemsByEventId(Long eventId);

} 