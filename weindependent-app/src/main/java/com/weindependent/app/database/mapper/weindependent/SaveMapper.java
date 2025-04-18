package com.weindependent.app.database.mapper.weindependent;

import java.util.List;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.SaveListDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * @author Elly
 * 2025-04-17
 */
@Mapper
public interface SaveMapper {

    /*收藏 */
    int saveBlog(@Param("listId") int listId, @Param("blogId") int blogId);

    /*取消收藏 */
    int unsaveBlog(@Param("listId") int listId, @Param("blogId") int blogId);
}
