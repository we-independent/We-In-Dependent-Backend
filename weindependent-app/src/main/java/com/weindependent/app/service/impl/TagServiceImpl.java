package com.weindependent.app.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.database.mapper.weindependent.TagMapper;
import com.weindependent.app.service.TagService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TagServiceImpl implements TagService{

    @Resource
    private TagMapper tagMapper;

    public List<TagDO> getAllTags() {
        return tagMapper.getAllTags();
    }

    public List<TagDO> getTagsByCategory(String category) {
        return tagMapper.getTagsByCategoryName(category);
    }
}
