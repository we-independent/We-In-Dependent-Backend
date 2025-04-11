package com.weindependent.app.database.mapper.weindependent;

import java.util.List;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
/**
 * @author Elly
 * 2025-04-13
 */
@Mapper
public interface SaveBlogMapper {
    int saveBlog(@Param("userId") int userId, @Param("blogId") int blogId);

    int unsaveBlog(@Param("userId") int userId, @Param("blogId") int blogId);

    List<BlogArticleDO> getSavedBlogs(@Param("userId") int userId);
}
