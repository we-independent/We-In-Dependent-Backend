package com.weindependent.app.service;

import java.util.List;

import com.weindependent.app.vo.event.EventSpeakerVO;

public interface IEventSpeakerService {
    EventSpeakerVO getById(Long id);
    List<EventSpeakerVO> searchByName(String name);
}
