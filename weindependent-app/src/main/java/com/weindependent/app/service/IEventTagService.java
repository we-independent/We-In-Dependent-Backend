package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.EventTagDO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IEventTagService {
    List<EventTagDO> getAllTags();
}
