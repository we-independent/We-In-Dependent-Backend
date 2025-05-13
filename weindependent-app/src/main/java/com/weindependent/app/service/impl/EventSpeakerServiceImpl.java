package com.weindependent.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.weindependent.app.database.mapper.weindependent.EventSpeakerMapper;
import com.weindependent.app.service.IEventSpeakerService;
import com.weindependent.app.vo.event.EventSpeakerVO;

@Service
public class EventSpeakerServiceImpl implements IEventSpeakerService {

    @Resource
    private EventSpeakerMapper eventSpeakerMapper;

    @Override
    public EventSpeakerVO getById(Long id) {
        EventSpeakerVO speaker = eventSpeakerMapper.getById(id);
        if (speaker == null) return null;

        EventSpeakerVO vo = new EventSpeakerVO();
        BeanUtils.copyProperties(speaker, vo);
        // Fill name and bannerUrl via join or custom result mapping
        return vo;
    }

    @Override
    public List<EventSpeakerVO> searchByName(String name) {
        List<EventSpeakerVO> result = eventSpeakerMapper.searchByName(name);
        return result.stream().map(s -> {
            EventSpeakerVO vo = new EventSpeakerVO();
            BeanUtils.copyProperties(s, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}