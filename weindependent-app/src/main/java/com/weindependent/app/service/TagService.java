package com.weindependent.app.service;

import java.util.List;

import com.weindependent.app.database.dataobject.TagDO;

public interface TagService {
    List<TagDO> getAllTags();
    List<TagDO> getTagsByCategory(String category);
    
    //Hurely add for related article
    List<TagDO> getTagsByArticleId(Integer articleId);
    
}