package com.weindependent.app.database.mapper.weindependent;

import org.apache.ibatis.annotations.Mapper;

import com.weindependent.app.database.dataobject.TagDO;

import java.util.List;

@Mapper
public interface TagMapper {
    List<TagDO> getAllTags();
    List<TagDO> getTagsByCategoryName(String category);
} 