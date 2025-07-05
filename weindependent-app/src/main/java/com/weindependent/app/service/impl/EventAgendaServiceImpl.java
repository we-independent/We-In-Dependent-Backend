package com.weindependent.app.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.database.dataobject.EventAgendaDO;
import com.weindependent.app.database.mapper.weindependent.EventAgendaMapper;
import com.weindependent.app.dto.EventAgendaCreateQry;
import com.weindependent.app.dto.EventAgendaUpdateQry;
import com.weindependent.app.dto.EventAgendasWithItemsQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IEventAgendaItemService;
import com.weindependent.app.service.IEventAgendaService;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.vo.event.EventAgendaItemVO;
import com.weindependent.app.vo.event.EventAgendaVO;
import com.weindependent.app.vo.event.EventVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventAgendaServiceImpl implements IEventAgendaService {

    @Resource
    private EventAgendaMapper eventAgendaMapper;

    @Resource
    private IEventAgendaItemService eventAgendaItemService;

    @Resource
    private IEventService eventService;


    @Override
    public EventAgendaVO getAgendaByEventId(Long eventId) {
        EventAgendaDO agendaDO = eventAgendaMapper.getByEventId(eventId);
        if (agendaDO == null) {
            return null;
        }

        // 获取event信息
        EventVO eventVO = eventService.getEventById(eventId);
        if (eventVO == null) {
            throw new ResponseException(ErrorCode.NOT_FOUND.getCode(), "Event not found");
        }

        EventAgendaVO agendaVO = convertToVO(agendaDO, eventVO);
        List<EventAgendaItemVO> items = eventAgendaItemService.getItemsByAgendaId(agendaDO.getId());
        agendaVO.setItems(items);
        
        return agendaVO;
    }

    @Override
    @Transactional
    public EventAgendaVO createAgenda(EventAgendaCreateQry qry) {
        // 检查是否已存在
        EventAgendaDO existingAgenda = eventAgendaMapper.getByEventId(qry.getEventId());
        if (existingAgenda != null) {
            throw new ResponseException(ErrorCode.BAD_REQUEST.getCode(), "Agenda already exists for this event");
        }

        // 验证event是否存在
        EventVO eventVO = eventService.getEventById(qry.getEventId());
        if (eventVO == null) {
            throw new ResponseException(ErrorCode.NOT_FOUND.getCode(), "Event not found");
        }

        EventAgendaDO agendaDO = new EventAgendaDO();
        agendaDO.setEventId(qry.getEventId());
        agendaDO.setTitle(qry.getTitle());
        agendaDO.setDescription(qry.getDescription());
        agendaDO.setDeleted(false);
        agendaDO.setCreateTime(LocalDateTime.now());
        agendaDO.setUpdateTime(LocalDateTime.now());
        agendaDO.setCreateUserId(StpUtil.getLoginIdAsLong());
        agendaDO.setUpdateUserId(StpUtil.getLoginIdAsLong());

        eventAgendaMapper.insert(agendaDO);

        return convertToVO(agendaDO, eventVO);
    }

    @Override
    @Transactional
    public EventAgendaVO updateAgenda(EventAgendaUpdateQry qry) {
        EventAgendaDO agendaDO = eventAgendaMapper.getById(qry.getId());
        if (agendaDO == null) {
            throw new ResponseException(ErrorCode.NOT_FOUND.getCode(), "Agenda not found");
        }

        // 获取event信息
        EventVO eventVO = eventService.getEventById(agendaDO.getEventId());
        if (eventVO == null) {
            throw new ResponseException(ErrorCode.NOT_FOUND.getCode(), "Event not found");
        }

        agendaDO.setTitle(qry.getTitle());
        agendaDO.setDescription(qry.getDescription());
        agendaDO.setUpdateTime(LocalDateTime.now());
        agendaDO.setUpdateUserId(StpUtil.getLoginIdAsLong());

        eventAgendaMapper.update(agendaDO);

        return convertToVO(agendaDO, eventVO);
    }

    @Override
    @Transactional
    public void deleteAgenda(Long id) {
        EventAgendaDO agendaDO = eventAgendaMapper.getById(id);
        if (agendaDO == null) {
            throw new ResponseException(ErrorCode.NOT_FOUND.getCode(), "Agenda not found");
        }

        // 删除相关的agenda items
        eventAgendaItemService.deleteItemsByAgendaId(id);
        
        // 删除agenda
        eventAgendaMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAgendaByEventId(Long eventId) {
        EventAgendaDO agendaDO = eventAgendaMapper.getByEventId(eventId);
        if (agendaDO != null) {
            // 删除相关的agenda items
            eventAgendaItemService.deleteItemsByAgendaId(agendaDO.getId());
            
            // 删除agenda
            eventAgendaMapper.deleteByEventId(eventId);
        }
    }

    private EventAgendaVO convertToVO(EventAgendaDO agendaDO, EventVO eventVO) {
        EventAgendaVO agendaVO = new EventAgendaVO();
        agendaVO.setId(agendaDO.getId());
        agendaVO.setEventId(agendaDO.getEventId());
        agendaVO.setTitle(agendaDO.getTitle());
        agendaVO.setDescription(agendaDO.getDescription());
        agendaVO.setEventStartTime(eventVO.getEventTime());
        agendaVO.setEventDuration(eventVO.getDuration());
        agendaVO.setCreateTime(agendaDO.getCreateTime());
        agendaVO.setUpdateTime(agendaDO.getUpdateTime());
        return agendaVO;
    }

    @Override
    public EventAgendasWithItemsQry getAgendasWithItemsByEventId(Long eventId){
        return eventAgendaMapper.getAgendasWithItemsByEventId(eventId);
    }

} 