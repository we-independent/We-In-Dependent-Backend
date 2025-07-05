package com.weindependent.app.service.impl;



import com.weindependent.app.database.dataobject.EventTagDO;
import com.weindependent.app.database.mapper.weindependent.EventTagMapper;
import com.weindependent.app.service.IEventTagService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Service
public class EventTagServiceImpl implements IEventTagService {
    @Resource
    private EventTagMapper eventTagMapper;

    @Override
    public List<EventTagDO> getAllTags() {
        return eventTagMapper.getAllTags();
    }
}