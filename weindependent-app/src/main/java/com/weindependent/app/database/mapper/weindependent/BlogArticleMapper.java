package com.weindependent.app.database.mapper.weindependent;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.weindependent.app.database.dataobject.BlogArticleDO;

import java.util.List;

@Mapper
public interface BlogArticleMapper {
    BlogArticleDO findById(@Param("id") Integer id);

    List<BlogArticleDO> findByIds(@Param("ids") List<Integer> ids);

    List<BlogArticleDO> findPublishedArticles(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
