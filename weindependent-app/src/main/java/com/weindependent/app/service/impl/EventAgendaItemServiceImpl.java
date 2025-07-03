package com.weindependent.app.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.database.dataobject.EventAgendaDO;
import com.weindependent.app.database.dataobject.EventAgendaItemDO;
import com.weindependent.app.database.mapper.weindependent.EventAgendaMapper;
import com.weindependent.app.database.mapper.weindependent.EventAgendaItemMapper;
import com.weindependent.app.dto.EventAgendaItemCreateQry;
import com.weindependent.app.dto.EventAgendaItemUpdateQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IEventAgendaItemService;
import com.weindependent.app.service.IEventService;
import com.weindependent.app.vo.event.EventAgendaItemVO;
import com.weindependent.app.vo.event.EventAgendaVO;
import com.weindependent.app.vo.event.EventVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventAgendaItemServiceImpl implements IEventAgendaItemService {

    @Resource
    private EventAgendaItemMapper eventAgendaItemMapper;

    @Resource
    private EventAgendaMapper eventAgendaMapper;

    @Resource
    private IEventService eventService;

    @Override
    public List<EventAgendaItemVO> getItemsByAgendaId(Long agendaId) {
        List<EventAgendaItemDO> items = eventAgendaItemMapper.getByAgendaId(agendaId);
        return items.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public EventAgendaItemVO getItemById(Long id) {
        EventAgendaItemDO itemDO = eventAgendaItemMapper.getById(id);
        if (itemDO == null) {
            return null;
        }
        return convertToVO(itemDO);
    }

    @Override
    @Transactional
    public EventAgendaItemVO createItem(EventAgendaItemCreateQry qry) {
        // 验证时间逻辑
        if (qry.getStartTime().isAfter(qry.getEndTime())) {
            throw new ResponseException(ErrorCode.BAD_REQUEST.getCode(), "Start time cannot be after end time");
        }

        // 验证agenda item时间是否在event时间范围内
        validateAgendaItemTime(qry.getAgendaId(), qry.getStartTime(), qry.getEndTime());

        EventAgendaItemDO itemDO = new EventAgendaItemDO();
        itemDO.setAgendaId(qry.getAgendaId());
        itemDO.setTitle(qry.getTitle());
        itemDO.setDescription(qry.getDescription());
        itemDO.setStartTime(qry.getStartTime());
        itemDO.setEndTime(qry.getEndTime());
        
        // 计算持续时间
        if (qry.getDurationMinutes() == null) {
            long durationMinutes = ChronoUnit.MINUTES.between(qry.getStartTime(), qry.getEndTime());
            itemDO.setDurationMinutes((int) durationMinutes);
        } else {
            itemDO.setDurationMinutes(qry.getDurationMinutes());
        }
        
        itemDO.setSortOrder(qry.getSortOrder() != null ? qry.getSortOrder() : 0);
        itemDO.setDeleted(false);
        itemDO.setCreateTime(LocalDateTime.now());
        itemDO.setUpdateTime(LocalDateTime.now());
        itemDO.setCreateUserId(StpUtil.getLoginIdAsLong());
        itemDO.setUpdateUserId(StpUtil.getLoginIdAsLong());

        eventAgendaItemMapper.insert(itemDO);

        return convertToVO(itemDO);
    }

    @Override
    @Transactional
    public EventAgendaItemVO updateItem(EventAgendaItemUpdateQry qry) {
        EventAgendaItemDO itemDO = eventAgendaItemMapper.getById(qry.getId());
        if (itemDO == null) {
            throw new ResponseException(ErrorCode.NOT_FOUND.getCode(), "Agenda item not found");
        }

        // 验证时间逻辑
        if (qry.getStartTime().isAfter(qry.getEndTime())) {
            throw new ResponseException(ErrorCode.BAD_REQUEST.getCode(), "Start time cannot be after end time");
        }

        // 验证agenda item时间是否在event时间范围内
        validateAgendaItemTime(itemDO.getAgendaId(), qry.getStartTime(), qry.getEndTime());

        itemDO.setTitle(qry.getTitle());
        itemDO.setDescription(qry.getDescription());
        itemDO.setStartTime(qry.getStartTime());
        itemDO.setEndTime(qry.getEndTime());
        
        // 计算持续时间
        if (qry.getDurationMinutes() == null || qry.getDurationMinutes() <= 0) {
            long durationMinutes = ChronoUnit.MINUTES.between(qry.getStartTime(), qry.getEndTime());
            itemDO.setDurationMinutes((int) durationMinutes);
        } else {
            itemDO.setDurationMinutes(qry.getDurationMinutes());
        }
        
        itemDO.setSortOrder(qry.getSortOrder() != null ? qry.getSortOrder() : itemDO.getSortOrder());
        itemDO.setUpdateTime(LocalDateTime.now());
        itemDO.setUpdateUserId(StpUtil.getLoginIdAsLong());

        eventAgendaItemMapper.update(itemDO);

        return convertToVO(itemDO);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        EventAgendaItemDO itemDO = eventAgendaItemMapper.getById(id);
        if (itemDO == null) {
            throw new ResponseException(ErrorCode.NOT_FOUND.getCode(), "Agenda item not found");
        }

        eventAgendaItemMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteItemsByAgendaId(Long agendaId) {
        eventAgendaItemMapper.deleteByAgendaId(agendaId);
    }

    @Override
    @Transactional
    public void updateItemSortOrder(Long id, Integer sortOrder) {
        EventAgendaItemDO itemDO = eventAgendaItemMapper.getById(id);
        if (itemDO == null) {
            throw new ResponseException(ErrorCode.NOT_FOUND.getCode(), "Agenda item not found");
        }

        eventAgendaItemMapper.updateSortOrder(id, sortOrder);
    }

    /**
     * 验证agenda item的时间是否在event的时间范围内
     */
    private void validateAgendaItemTime(Long agendaId, LocalTime startTime, LocalTime endTime) {
        // 获取agenda信息
        EventAgendaDO agendaDO = eventAgendaMapper.getById(agendaId);
        if (agendaDO == null) {
            throw new ResponseException(ErrorCode.NOT_FOUND.getCode(), "Agenda not found");
        }

        // 获取event信息
        EventVO eventVO = eventService.getEventById(agendaDO.getEventId());
        if (eventVO == null) {
            throw new ResponseException(ErrorCode.NOT_FOUND.getCode(), "Event not found");
        }

        // 将event的开始时间转换为LocalTime
        LocalTime eventStartTime = eventVO.getEventTime()
                .atZone(ZoneId.systemDefault())
                .toLocalTime();

        // 计算event的结束时间
        LocalTime eventEndTime = eventStartTime.plusMinutes(eventVO.getDuration());

        // 验证agenda item的时间是否在event时间范围内
        if (startTime.isBefore(eventStartTime) || endTime.isAfter(eventEndTime)) {
            throw new ResponseException(ErrorCode.BAD_REQUEST.getCode(), 
                "Agenda item time must be within event time range. Event time: " + 
                eventStartTime + " - " + eventEndTime);
        }
    }

    private EventAgendaItemVO convertToVO(EventAgendaItemDO itemDO) {
        EventAgendaItemVO itemVO = new EventAgendaItemVO();
        itemVO.setId(itemDO.getId());
        itemVO.setAgendaId(itemDO.getAgendaId());
        itemVO.setTitle(itemDO.getTitle());
        itemVO.setDescription(itemDO.getDescription());
        itemVO.setStartTime(itemDO.getStartTime());
        itemVO.setEndTime(itemDO.getEndTime());
        itemVO.setDurationMinutes(itemDO.getDurationMinutes());
        itemVO.setSortOrder(itemDO.getSortOrder());
        itemVO.setCreateTime(itemDO.getCreateTime());
        itemVO.setUpdateTime(itemDO.getUpdateTime());
        return itemVO;
    }
} 